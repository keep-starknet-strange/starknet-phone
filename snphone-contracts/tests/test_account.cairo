use snforge_std::{declare, ContractClassTrait};
use snforge_std::{start_prank, stop_prank, CheatTarget};
use starknet::contract_address_const;
use contracts::account::{IStarknetPhoneAccountDispatcher, IStarknetPhoneAccountDispatcherTrait};

#[test]
fn test_deploy() {
    let class_hash = declare("StarknetPhoneAccount");
    let _pub_key = 'pub_key';
    let contract_address = class_hash.deploy(@array![_pub_key]).unwrap();
    let wallet = IStarknetPhoneAccountDispatcher { contract_address };

    let pub_key = wallet.get_public_key();
    assert(pub_key == _pub_key, 'Pub key not set');
}

// Test that only the contract owner can change the public key
#[test]
fn test_only_account_can_change_public_key() {
    let class_hash = declare("StarknetPhoneAccount");
    let _pub_key = 'pub_key';
    let contract_address = class_hash.deploy(@array![_pub_key]).unwrap();
    let wallet = IStarknetPhoneAccountDispatcher { contract_address };

    // Other contract calls function
    let new_pub_key = 'new_pub_key';

    start_prank(CheatTarget::One(wallet.contract_address), contract_address);
    wallet.set_public_key(new_pub_key);
    stop_prank(CheatTarget::One(wallet.contract_address));

    assert(wallet.get_public_key() == new_pub_key, 'Pub key should change');
}

// Test only the wallet can change its public key
#[test]
#[should_panic]
fn test_other_account_cannot_change_public_key() {
    let class_hash = declare("StarknetPhoneAccount");
    let _pub_key = 'pub_key';
    let contract_address = class_hash.deploy(@array![_pub_key]).unwrap();
    let wallet = IStarknetPhoneAccountDispatcher { contract_address };

    // Other contract calls function
    let not_wallet = contract_address_const::<'not_wallet'>();
    let new_pub_key = 'new_pub_key';

    start_prank(CheatTarget::One(wallet.contract_address), not_wallet);
    wallet.set_public_key(new_pub_key);
    stop_prank(CheatTarget::One(wallet.contract_address));

    assert(wallet.get_public_key() != new_pub_key, 'Pub key should not change');
}
//#[test]
//fn test_is_valid_signature() { // TODO: Test is_valid_signature() works as expected (valid returns true, anything else returns false (check 0 hash and empty sigs as well))
//}

//#[test]
//fn test_execute() { // TODO: Test __execute__() works as expected (solo and multi-calls should work as expected)
//        - Might need to create a mock erc20 contract to test calls (see if the wallet is able to do a multi call (try sending eth to 2 accounts from the 
//          deployed wallet, both accounts' balance should update)
//}


