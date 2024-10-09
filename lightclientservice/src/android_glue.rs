use std::{
    net::{IpAddr, Ipv4Addr, SocketAddr},
    path::PathBuf,
    sync::Arc,
    time::Duration,
};

use super::config::Config;
use helios::config::networks::Network;
use tokio::sync::RwLock;

use j4rs::prelude::*;
use j4rs::InvocationArg;
use j4rs_derive::*;

use j4rs::jni_sys::{jint, JavaVM};

const RPC_SPEC_VERSION: &str = "0.6.0";

const JNI_VERSION_1_6: jint = 0x00010006;

#[allow(non_snake_case)]
#[no_mangle]
pub extern "C" fn jni_onload(env: *mut JavaVM, _reserved: jobject) -> jint {
    j4rs::set_java_vm(env);
    JNI_VERSION_1_6
}

#[call_from_java("com.snphone.lightclientservice.BeerusClient.run")]
fn run_wrapper(
    eth_execution_rpc_instance: Instance,
    starknet_rpc_instance: Instance,
) -> Result<Instance, String> {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let eth_execution_rpc: String = jvm.to_rust(eth_execution_rpc_instance).unwrap();
    let starknet_rpc: String = jvm.to_rust(starknet_rpc_instance).unwrap();

    println!(
        "Starting Beerus with:\nETH RPC: {}\nSTARKNET RPC: {}",
        eth_execution_rpc, starknet_rpc
    );

    let rt = tokio::runtime::Builder::new_current_thread()
        .enable_all()
        .build()
        .unwrap();

    let result = rt.block_on(sleep_and_return());
    println!("Sleep function completed: {}", result);
    InvocationArg::try_from(result)
        .map_err(|error| format!("{}", error))
        .and_then(|arg| Instance::try_from(arg).map_err(|error| format!("{}", error)))

    /*
    let run_response = rt.block_on(run(
        eth_execution_rpc,
        starknet_rpc,
        String::from("tmp"),
        5,
        8080,
    ));

    match run_response {
        Ok(_) => {
            println!("Beerus client ran successfully");
            InvocationArg::try_from("Beerus client started")
                .map_err(|error| format!("{}", error))
                .and_then(|arg| Instance::try_from(arg).map_err(|error| format!("{}", error)))
        }
        Err(e) => {
            eprintln!("Error running Beerus client: {}", e);
            Err(format!("Error running Beerus client: {}", e))
        }
    }
    */
}

#[call_from_java("com.snphone.lightclientservice.BeerusClient.echo")]
fn echo(message_instance: Instance) -> Result<Instance, String> {
    let jvm: Jvm = Jvm::attach_thread().unwrap();
    let message: String = jvm.to_rust(message_instance).unwrap();

    let ia = InvocationArg::try_from(message)
        .map_err(|error| format!("{}", error))
        .unwrap();
    Instance::try_from(ia).map_err(|error| format!("{}", error))
}

// This is just for verifying async behvaior in android
async fn sleep_and_return() -> String {
    tokio::time::sleep(tokio::time::Duration::from_secs(5)).await;
    "Slept for 5 seconds".to_string()
}

// NOTE: this should probably run in its own thread?
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

/*
#[no_mangle]
pub extern "C" fn Java_com_snphone_lightclientservice_BeerusClient_run<
    'local,
>(
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

    //run(eth_execution_rpc, starknet_rpc, data_dir, 5, 8080);
    let run_result = run(eth_execution_rpc, starknet_rpc);
    // creating a new java string to return to our android app
    let output =
        env.new_string(run_result).expect("Couldn't create a java string!");

    output.into_raw()
}
*/
