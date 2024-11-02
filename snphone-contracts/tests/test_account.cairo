use snforge_std::{declare, ContractClassTrait, ContractClass, DeclareResultTrait, start_cheat_caller_address, stop_cheat_caller_address, prank, CheatSpan};
use openzeppelin::token::erc20::interface::{IERC20Dispatcher, IERC20DispatcherTrait};
use starknet::contract_address_const;
use starknet::{ContractAddress, {account::Call}};
use contracts::account::{IStarknetPhoneAccountDispatcher, IStarknetPhoneAccountDispatcherTrait};

const PUB_KEY: felt252 = 'pub_key';
const INITIAL_SUPPLY: u256 = 1000;

fn OWNER() -> ContractAddress {
    'OWNER'.try_into().unwrap()
}

fn RECIPIENT() -> ContractAddress {
    'RECIPIENT'.try_into().unwrap()
}

fn BOB() -> ContractAddress {
    'BOB'.try_into().unwrap()
}

fn ALICE() -> ContractAddress {
    'ALICE'.try_into().unwrap()
}

fn deploy_erc20_mock(name: ByteArray, symbol: ByteArray,) -> IERC20Dispatcher {
    let class = declare("ERC20Mock");
    let mut calldata = array![];
    name.serialize(ref calldata);
    symbol.serialize(ref calldata);
    INITIAL_SUPPLY.serialize(ref calldata);
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

fn _setup() -> (IStarknetPhoneAccountDispatcher, IERC20Dispatcher) {
    let mock_erc20_dispatcher = deploy_erc20_mock("mock one", "MCK1");
    let wallet_dispatcher = deploy_wallet();

    (wallet_dispatcher, mock_erc20_dispatcher)
}

fn craft_erc20_transfer_call(
    to: ContractAddress, recipient: ContractAddress, amount: u256
) -> Call {
    let mut calldata = array![];
    recipient.serialize(ref calldata);
    amount.serialize(ref calldata);

    Call { to, selector: selector!("transfer"), calldata: calldata.span() }
}

#[test]
fn test_deploy() {
    let (wallet, _) = _setup();

    let pub_key = wallet.get_public_key();
    assert(pub_key == PUB_KEY, 'Pub key not set');
}

// Test that only the contract owner can change the public key
#[test]
fn test_only_account_can_change_public_key() {
    let (wallet, _) = _setup();

    // Other contract calls function
    let new_pub_key = 'new_pub_key';

    start_cheat_caller_address(wallet.contract_address, contract_address);
    wallet.set_public_key(new_pub_key);
    stop_cheat_caller_address(wallet.contract_address);

    assert(wallet.get_public_key() == new_pub_key, 'Pub key should change');
}

// Test only the wallet can change its public key
#[test]
#[should_panic]
fn test_other_account_cannot_change_public_key() {
    let (wallet, _) = _setup();

    // Other contract calls function
    let not_wallet = contract_address_const::<'not_wallet'>();
    let new_pub_key = 'new_pub_key';

    start_cheat_caller_address(wallet.contract_address, contract_address);
    wallet.set_public_key(new_pub_key);
    stop_cheat_caller_address(wallet.contract_address);

    assert(wallet.get_public_key() != new_pub_key, 'Pub key should not change');
}
//#[test]
//fn test_is_valid_signature() { // TODO: Test is_valid_signature() works as expected (valid returns true, anything else returns false (check 0 hash and empty sigs as well))
//}

#[test]
#[should_panic(expected: ('invalid caller',))]
fn test_execute_with_invalid_caller() {
    let (wallet, mock_erc20) = _setup();

    // Other contract calls function
    let not_wallet = contract_address_const::<'not_wallet'>();

    // Craft call and add to calls array
    let amount = 200;
    let mut calldata = array![];
    mock_erc20.contract_address.serialize(ref calldata);
    RECIPIENT().serialize(ref calldata);
    amount.serialize(ref calldata);

    let call = Call {
        to: mock_erc20.contract_address, selector: selector!("transfer"), calldata: calldata.span()
    };

    let calls = array![call];

    start_prank(CheatTarget::One(wallet.contract_address), not_wallet);
    wallet.__execute__(calls);
    stop_prank(CheatTarget::One(wallet.contract_address));
}

#[test]
fn test_execute() {
    let (wallet, mock_erc20) = _setup();

    // fund wallet
    start_prank(CheatTarget::One(mock_erc20.contract_address), OWNER());
    mock_erc20.transfer(wallet.contract_address, INITIAL_SUPPLY);
    stop_prank(CheatTarget::One(mock_erc20.contract_address));

    // Craft call and add to calls array
    let amount = 200_u256;
    let call = craft_erc20_transfer_call(mock_erc20.contract_address, RECIPIENT(), amount);

    let calls = array![call];

    let zero = contract_address_const::<0>();

    let wallet_ballance_before = mock_erc20.balance_of(wallet.contract_address);

    // execute
    start_prank(CheatTarget::One(wallet.contract_address), zero);
    wallet.__execute__(calls);
    stop_prank(CheatTarget::One(wallet.contract_address));

    let wallet_ballance_after = mock_erc20.balance_of(wallet.contract_address);

    assert((wallet_ballance_before - amount) == wallet_ballance_after, 'wrong wallet balance');
    assert(mock_erc20.balance_of(RECIPIENT()) == amount, 'wrong recipient balance');
}

#[test]
fn test_multicall() {
    let (wallet, mock_erc20) = _setup();

    // fund wallet
    start_prank(CheatTarget::One(mock_erc20.contract_address), OWNER());
    mock_erc20.transfer(wallet.contract_address, INITIAL_SUPPLY);
    stop_prank(CheatTarget::One(mock_erc20.contract_address));

    let first_amount = 300_u256;
    let second_amount = 100_u256;
    let third_amount = 150_u256;
    let forth_amount = 50_u256;

    // Craft call and add to calls array
    let first_call = craft_erc20_transfer_call(
        mock_erc20.contract_address, RECIPIENT(), first_amount
    );
    let second_call = craft_erc20_transfer_call(
        mock_erc20.contract_address, OWNER(), second_amount
    );
    let third_call = craft_erc20_transfer_call(mock_erc20.contract_address, BOB(), third_amount);
    let forth_call = craft_erc20_transfer_call(mock_erc20.contract_address, ALICE(), forth_amount);

    let calls = array![first_call, second_call, third_call, forth_call];

    let zero = contract_address_const::<0>();

    // execute
    start_prank(CheatTarget::One(wallet.contract_address), zero);
    wallet.__execute__(calls);
    stop_prank(CheatTarget::One(wallet.contract_address));

    let wallet_ballance_after = mock_erc20.balance_of(wallet.contract_address);
    let expected_wallet_balance = INITIAL_SUPPLY
        - first_amount
        - second_amount
        - third_amount
        - forth_amount;

    assert(wallet_ballance_after == expected_wallet_balance, 'wrong wallet balance');
    assert(mock_erc20.balance_of(RECIPIENT()) == first_amount, 'wrong recipient balance');
    assert(mock_erc20.balance_of(OWNER()) == second_amount, 'wrong owner balance');
    assert(mock_erc20.balance_of(BOB()) == third_amount, 'wrong bob balance');
    assert(mock_erc20.balance_of(ALICE()) == forth_amount, 'wrong alice balance');
}
