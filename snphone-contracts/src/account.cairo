use starknet::{account::Call};

#[starknet::interface]
pub trait IStarknetPhoneAccount<TContractState> {
    fn get_public_key(self: @TContractState) -> felt252;
    fn set_public_key(ref self: TContractState, new_public_key: felt252);
    fn is_valid_signature(
        self: @TContractState, hash: felt252, signature: Span<felt252>
    ) -> felt252;
    fn __validate__(ref self: TContractState, calls: Array<Call>) -> felt252;
    fn __validate_declare__(self: @TContractState, class_hash: felt252) -> felt252;
    fn __validate_deploy__(ref self: TContractState, public_key: felt252) -> felt252;
    fn __execute__(ref self: TContractState, calls: Array<Call>) -> Array<Span<felt252>>;
}

#[starknet::contract(account)]
pub mod StarknetPhoneAccount {
    //use starknet::storage::StoragePointerWriteAccess;
    //use starknet::storage::StoragePointerReadAccess;
    use starknet::{
        get_tx_info, get_caller_address, get_contract_address, account::Call,
        syscalls::call_contract_syscall,
    };
    use core::ecdsa::check_ecdsa_signature;

    #[storage]
    struct Storage {
        _public_key: felt252,
    }

    #[constructor]
    fn constructor(ref self: ContractState, public_key: felt252) {
        self._public_key.write(public_key);
    }

    #[abi(embed_v0)]
    impl IAccountImpl of super::IStarknetPhoneAccount<ContractState> {
        fn get_public_key(self: @ContractState) -> felt252 {
            self._public_key.read()
        }

        fn set_public_key(ref self: ContractState, new_public_key: felt252) {
            self.assert_only_self();
            self._public_key.write(new_public_key);
        }

        fn is_valid_signature(
            self: @ContractState, hash: felt252, signature: Span<felt252>
        ) -> felt252 {
            self._is_valid_signature(hash, signature)
        }

        fn __validate_deploy__(ref self: ContractState, public_key: felt252) -> felt252 {
            self.validate_transaction()
        }

        fn __validate_declare__(self: @ContractState, class_hash: felt252) -> felt252 {
            self.validate_transaction()
        }

        fn __validate__(ref self: ContractState, mut calls: Array<Call>) -> felt252 {
            self.validate_transaction()
        }

        fn __execute__(ref self: ContractState, mut calls: Array<Call>) -> Array<Span<felt252>> {
            let caller = get_caller_address();
            assert(caller.is_zero(), 'invalid caller');

            let tx_info = get_tx_info().unbox();
            assert(tx_info.version != 0, 'invalid tx version');

            self._execute_calls(calls)
        }
    }

    #[generate_trait]
    impl internalImpl of InternalTrait {
        fn assert_only_self(ref self: ContractState) {
            let caller = get_caller_address();
            let self = get_contract_address();
            assert(self == caller, 'Account: unathorized');
        }

        fn validate_transaction(self: @ContractState) -> felt252 {
            let tx_info = get_tx_info().unbox();
            let tx_hash = tx_info.transaction_hash;
            let signature = tx_info.signature;
            assert(
                self._is_valid_signature(tx_hash, signature) == starknet::VALIDATED,
                'Account: invalid signature'
            );
            starknet::VALIDATED
        }

        fn _is_valid_signature(
            self: @ContractState, hash: felt252, signature: Span<felt252>
        ) -> felt252 {
            assert(signature.len() == 2_u32, 'invalid signature');
            let public_key = self._public_key.read();

            // TODO: update this to check valid secp256k1 signature -- use argent signers
            // implementation as reference

            if check_ecdsa_signature(
                message_hash: hash,
                public_key: public_key,
                signature_r: *signature[0_u32],
                signature_s: *signature[1_u32],
            ) {
                return starknet::VALIDATED;
            } else {
                return 0;
            }
        }

        fn _execute_calls(ref self: ContractState, mut calls: Array<Call>) -> Array<Span<felt252>> {
            let mut result: Array<Span<felt252>> = ArrayTrait::new();
            let mut calls = calls;

            loop {
                match calls.pop_front() {
                    Option::Some(call) => {
                        match call_contract_syscall(call.to, call.selector, call.calldata) {
                            Result::Ok(mut retdata) => { result.append(retdata); },
                            Result::Err(_) => { panic(array!['multicall_failed']); }
                        }
                    },
                    Option::None(_) => { break (); }
                };
            };
            result
        }
    }
}
