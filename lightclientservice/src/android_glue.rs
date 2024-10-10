use std::{net::SocketAddr, path::PathBuf, sync::Arc, time::Duration};

use super::config::Config;
use helios::config::networks::Network;
use tokio::sync::RwLock;

use jni::objects::{JClass, JString};
use jni::JNIEnv;

use jni::sys::jstring;

const RPC_SPEC_VERSION: &str = "0.6.0";

// This is just for verifying async behvaior in android
async fn sleep_and_return() {
    tokio::time::sleep(tokio::time::Duration::from_secs(3)).await;
}

async fn run(
    eth_execution_rpc: String,
    starknet_rpc: String,
    data_dir: String,
    poll_secs: u64,
    socket_port: u16,
) -> eyre::Result<()> {
    println!("DEBUG: Starting run function");

    println!("DEBUG: Creating config");
    let config = Config {
        network: Network::SEPOLIA, // TODO: take network as input
        eth_execution_rpc,
        starknet_rpc,
        data_dir: PathBuf::from(data_dir),
        poll_secs,
        rpc_addr: SocketAddr::from(([127, 0, 0, 1], socket_port)),
    };
    println!("DEBUG: Checking config");
    config.check().await?;

    println!("DEBUG: Creating new Beerus client");
    let beerus = super::client::Client::new(&config).await?;
    println!("DEBUG: Starting Beerus client");
    beerus.start().await?;

    println!("DEBUG: Getting RPC spec version");
    let rpc_spec_version = beerus.spec_version().await?;
    println!("DEBUG: RPC spec version: {}", rpc_spec_version);
    if rpc_spec_version != RPC_SPEC_VERSION {
        println!("DEBUG: RPC spec version mismatch");
        eyre::bail!(
            "RPC spec version mismatch: expected {RPC_SPEC_VERSION} but got {rpc_spec_version}"
        );
    }

    println!("DEBUG: Getting initial state");
    let state = beerus.get_state().await?;
    println!("DEBUG: Initial state: {:?}", state);

    let state = Arc::new(RwLock::new(state));

    {
        println!("DEBUG: Setting up state update task");
        let state = state.clone();
        let period = Duration::from_secs(config.poll_secs);
        tokio::spawn(async move {
            let mut tick = tokio::time::interval(period);
            loop {
                tick.tick().await;
                println!("DEBUG: Updating state");
                match beerus.get_state().await {
                    Ok(update) => {
                        *state.write().await = update;
                        println!("DEBUG: State updated successfully");
                    }
                    Err(e) => {
                        println!("DEBUG: State update failed: {:?}", e);
                    }
                }
            }
        });
    }

    println!("DEBUG: Starting RPC server");
    let server = super::rpc::serve(&config.starknet_rpc, &config.rpc_addr, state).await?;

    println!("DEBUG: RPC server started on port {}", server.port());
    server.done().await;

    println!("DEBUG: Run function completed");
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
            3030,
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

#[cfg(test)]
mod tests {
    use super::run;

    #[tokio::test]
    async fn test_run() {
        let eth_execution_rpc =
            "https://eth-sepolia.g.alchemy.com/v2/Fl4zNtN2hak5hrWq92a8pnB-ZospWX9a".to_string();
        let starknet_rpc = "https://starknet-sepolia.g.alchemy.com/starknet/version/rpc/v0_7/Fl4zNtN2hak5hrWq92a8pnB-ZospWX9a".to_string();
        let data_dir = String::from("tmp");

        let run_response = run(eth_execution_rpc, starknet_rpc, data_dir, 5, 3030).await;

        assert!(run_response.is_ok());
    }
}
