use core::result::ResultTrait;
use snforge_std::{declare, ContractClassTrait};

use contracts::account::{IStarknetPhoneAccountDispatcher, IStarknetPhoneAccountDispatcherTrait};

#[test]
fn test_deploy() {
    let contract = declare("StarknetPhoneAccount");
    let contract_address = contract.deploy(@array![]).unwrap();

    let dispatcher = IStarknetPhoneAccountDispatcher { contract_address };

    let pub_key = dispatcher.get_public_key();
    assert(pub_key == 0x0, 'balance == 0');
}
