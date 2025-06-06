use eyre::{Context, Result};
use tracing::debug;
use tracing_subscriber::fmt::format::debug_fn;

use crate::eth::EthereumClient;
use crate::gen::client::Client as StarknetClient;
use crate::gen::{BlockId, Felt, Rpc};
use crate::{config::Config, gen::FunctionCall};

#[derive(Debug, Clone)]
pub struct State {
    pub block_number: u64,
    pub block_hash: Felt,
    pub root: Felt,
}

pub struct Client {
    starknet: StarknetClient,
    ethereum: EthereumClient,
}

impl Client {
    pub async fn new(config: &Config) -> Result<Self> {
        debug!("Creating new client");
        let starknet = StarknetClient::new(&config.starknet_rpc);
        debug!("Created starknet client");
        let ethereum = EthereumClient::new(config).await?;
        debug!("Created ethereum client");
        Ok(Self { starknet, ethereum })
    }

    pub async fn start(&self) -> Result<()> {
        self.ethereum.start().await
    }

    pub async fn call_starknet(
        &self,
        request: FunctionCall,
        block_id: BlockId,
    ) -> Result<Vec<Felt>> {
        let ret = self.starknet.call(request, block_id).await?;
        Ok(ret)
    }

    pub async fn get_state(&self) -> Result<State> {
        let (block_number, block_hash, state_root) = self
            .ethereum
            .starknet_state()
            .await
            .context("beerus: get starknet state")?;

        Ok(State {
            block_number,
            block_hash: as_felt(block_hash.as_bytes())?,
            root: as_felt(state_root.as_bytes())?,
        })
    }

    pub async fn spec_version(&self) -> Result<String> {
        let version = self.starknet.specVersion().await?;
        Ok(version)
    }
}

fn as_felt(bytes: &[u8]) -> Result<Felt> {
    // RPC spec FELT regex: leading zeroes are not allowed
    let hex = hex::encode(bytes);
    let hex = hex.chars().skip_while(|c| c == &'0').collect::<String>();
    let hex = format!("0x{hex}");
    let felt = Felt::try_new(&hex)?;
    Ok(felt)
}
