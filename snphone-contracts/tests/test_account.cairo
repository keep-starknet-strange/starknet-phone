use snforge_std::{declare, ContractClassTrait, ContractClass};
use snforge_std::{start_prank, stop_prank, CheatTarget};
use openzeppelin::token::erc20::interface::{IERC20Dispatcher, IERC20DispatcherTrait};
use starknet::contract_address_const;
use starknet::{ContractAddress, {account::Call}};
use contracts::account::{IStarknetPhoneAccountDispatcher, IStarknetPhoneAccountDispatcherTrait};

const PUB_KEY: felt252 = 'pub_key';

fn OWNER() -> ContractAddress {
    'OWNER'.try_into().unwrap()
}

fn declare_erc20_mock() -> ContractClass {
    declare("ERC20Mock")
}

fn deploy_erc20_mock(
    class: ContractClass, name: ByteArray, symbol: ByteArray,
) -> IERC20Dispatcher {
    let mut calldata = array![];
    name.serialize(ref calldata);
    symbol.serialize(ref calldata);
    500_u256.serialize(ref calldata);
    OWNER().serialize(ref calldata);

    let contract_address = class.deploy(@calldata).unwrap();

    IERC20Dispatcher { contract_address }
}

fn deploy_wallet() -> IStarknetPhoneAccountDispatcher {
    let class_hash = declare("StarknetPhoneAccount");
    let contract_address = class_hash.deploy(@array![PUB_KEY]).unwrap();
    let wallet = IStarknetPhoneAccountDispatcher { contract_address };
    wallet
}

fn _setup() -> (IStarknetPhoneAccountDispatcher, IERC20Dispatcher, IERC20Dispatcher) {
    let mock_erc20_class = declare_erc20_mock();
    let mock_erc20_dispatcher_1 = deploy_erc20_mock(mock_erc20_class, "mock one", "MCK1");
    let mock_erc20_dispatcher_2 = deploy_erc20_mock(mock_erc20_class, "mock two", "MCK2");
    let wallet_dispatcher = deploy_wallet();

    (wallet_dispatcher, mock_erc20_dispatcher_1, mock_erc20_dispatcher_2)
}

#[test]
fn test_deploy() {
    let (wallet, _, _) = _setup();

    let pub_key = wallet.get_public_key();
    assert(pub_key == PUB_KEY, 'Pub key not set');
}

// Test that only the contract owner can change the public key
#[test]
fn test_only_account_can_change_public_key() {
    let (wallet, _, _) = _setup();

    // Other contract calls function
    let new_pub_key = 'new_pub_key';

    start_prank(CheatTarget::One(wallet.contract_address), wallet.contract_address);
    wallet.set_public_key(new_pub_key);
    stop_prank(CheatTarget::One(wallet.contract_address));

    assert(wallet.get_public_key() == new_pub_key, 'Pub key should change');
}

// Test only the wallet can change its public key
#[test]
#[should_panic]
fn test_other_account_cannot_change_public_key() {
    let (wallet, _, _) = _setup();

    // Other contract calls function
    let not_wallet = contract_address_const::<'not_wallet'>();
    let new_pub_key = 'new_pub_key';

    start_prank(CheatTarget::One(wallet.contract_address), not_wallet);
    wallet.set_public_key(new_pub_key);
    stop_prank(CheatTarget::One(wallet.contract_address));

    assert(wallet.get_public_key() != new_pub_key, 'Pub key should not change');
}

#[test]
fn test_is_valid_signature() { // TODO: Test is_valid_signature() works as expected (valid returns true, anything else returns false (check 0 hash and empty sigs as well))
}


#[test]
fn test_execute() { // TODO: Test __execute__() works as expected (solo and multi-calls should work as expected)
// //        - Might need to create a mock erc20 contract to test calls (see if the wallet is able to do a multi call (try sending eth to 2 accounts from the 
// //          deployed wallet, both accounts' balance should update)
}

