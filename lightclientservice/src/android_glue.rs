use std::{
    net::{IpAddr, Ipv4Addr, SocketAddr},
    path::PathBuf,
    sync::Arc,
    time::Duration,
};

use super::config::Config;
use helios::config::networks::Network;
use tokio::sync::RwLock;

use jni::objects::{JClass, JString};
use jni::JNIEnv;

use jni::sys::jstring;

const RPC_SPEC_VERSION: &str = "0.6.0";

// This is just for verifying async behvaior in android
async fn sleep_and_return() {
    tokio::time::sleep(tokio::time::Duration::from_secs(5)).await;
}

async fn run(
    eth_execution_rpc: String,
    starknet_rpc: String,
    data_dir: String,
    poll_secs: u64,
    socket_port: u16,
) -> eyre::Result<()> {
    tracing_subscriber::fmt::init();

    let config = Config {
        network: Network::SEPOLIA, // TODO: take network as input
        eth_execution_rpc,
        starknet_rpc,
        data_dir: PathBuf::from(data_dir),
        poll_secs,
        rpc_addr: SocketAddr::new(IpAddr::V4(Ipv4Addr::new(127, 0, 0, 1)), socket_port),
    };
    config.check().await?;

    let beerus = super::client::Client::new(&config).await?;
    beerus.start().await?;

    let rpc_spec_version = beerus.spec_version().await?;
    if rpc_spec_version != RPC_SPEC_VERSION {
        eyre::bail!(
            "RPC spec version mismatch: expected {RPC_SPEC_VERSION} but got {rpc_spec_version}"
        );
    }

    let state = beerus.get_state().await?;
    tracing::info!(?state, "initialized");

    let state = Arc::new(RwLock::new(state));

    {
        let state = state.clone();
        let period = Duration::from_secs(config.poll_secs);
        tokio::spawn(async move {
            let mut tick = tokio::time::interval(period);
            loop {
                tick.tick().await;
                match beerus.get_state().await {
                    Ok(update) => {
                        *state.write().await = update;
                        tracing::info!(?state, "updated");
                    }
                    Err(e) => {
                        tracing::error!(error=?e, "state update failed");
                    }
                }
            }
        });
    }

    let server = super::rpc::serve(&config.starknet_rpc, &config.rpc_addr, state).await?;

    tracing::info!(port = server.port(), "rpc server started");
    server.done().await;

    Ok(())
}

#[no_mangle]
pub extern "C" fn Java_com_snphone_lightclientservice_BeerusClient_echo<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    msg_input: JString<'local>,
) -> jstring {
    let msg: String = env
        .get_string(&msg_input)
        .expect("Unable to get message input")
        .into();

    // creating a new java string to return to our android app
    let output = env.new_string(msg).expect("Couldn't create a java string!");

    output.into_raw()
}

#[no_mangle]
pub extern "C" fn Java_com_snphone_lightclientservice_BeerusClient_echoBlock<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    msg_input: JString<'local>,
) -> jstring {
    let msg: String = env
        .get_string(&msg_input)
        .expect("Unable to get message input")
        .into();

    let runtime = tokio::runtime::Runtime::new().expect("Failed to create Tokio runtime");
    runtime.block_on(sleep_and_return());

    // creating a new java string to return to our android app
    let output = env.new_string(msg).expect("Couldn't create a java string!");

    output.into_raw()
}

#[no_mangle]
pub extern "C" fn Java_com_snphone_lightclientservice_BeerusClient_run<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    eth_execution_rpc: JString<'local>,
    starknet_rpc: JString<'local>,
) -> jstring {
    let eth_execution_rpc: String = env
        .get_string(&eth_execution_rpc)
        .expect("Unable to get eth_execution_rpc string")
        .into();

    let starknet_rpc: String = env
        .get_string(&starknet_rpc)
        .expect("Unable to get starknet_rpc string")
        .into();

    let runtime = tokio::runtime::Runtime::new().expect("Failed to create Tokio runtime");
    let run_result = runtime.block_on(async {
        match run(
            eth_execution_rpc,
            starknet_rpc,
            String::from("tmp"),
            5,
            8080,
        )
        .await
        {
            Ok(_) => "Beerus client run successfully".to_string(),
            Err(e) => format!("Error running Beerus client: {}", e),
        }
    });

    // creating a new java string to return to our android app
    let output = env
        .new_string(run_result)
        .expect("Couldn't create a java string!");

    output.into_raw()
}
