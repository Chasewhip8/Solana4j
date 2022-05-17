package dev.whips.solana4j.client;

public enum RPCMethod {
        GET_ACCOUNT_INFO("getAccountInfo"),
        GET_BALANCE("getBalance"),
        GET_BLOCK("getBlock"),
        GET_BLOCK_HEIGHT("getBlockHeight"),
        GET_BLOCK_PRODUCTION("getBlockProduction"),
        GET_BLOCK_COMMITMENT("getBlockCommitment"),
        GET_BLOCKS("getBlocks"),
        GET_BLOCKS_WITH_LIMIT("getBlocksWithLimit"),
        GET_BLOCK_TIME("getBlockTime"), // TODO
        GET_CLUSTER_NODES("getClusterNodes"),
        GET_EPOCH_INFO("getEpochInfo"),
        GET_EPOCH_SCHEDULE("getEpochSchedule"),
        GET_FEE_FOR_MESSAGE("getFeeForMessage"),
        GET_FIRST_AVAILABLE_BLOCK("getFirstAvailableBlock"),
        GET_GENESIS_HASH("getGenesisHash"),
        GET_HEALTH("getHealth"),
        GET_HIGHEST_SNAPSHOT_SLOT("getHighestSnapshotSlot"),
        GET_IDENTITY("getIdentity"),
        GET_INFLATION_GOVERNOR("getInflationGovernor"),
        GET_INFLATION_RATE("getInflationRate"),
        GET_INFLATION_REWARD("getInflationReward"),
        GET_LARGEST_ACCOUNTS("getLargestAccounts"),
        GET_LATEST_BLOCKHASH("getLatestBlockhash"), // TODO
        GET_LEADER_SCHEDULE("getLeaderSchedule"),
        GET_MAX_RETRANSMIT_SLOT("getMaxRetransmitSlot"),
        GET_MAX_SHRED_INSERT_SLOT("getMaxShredInsertSlot"),
        GET_MINIMUM_BALANCE_FOR_RENT_EXEMPTION("getMinimumBalanceForRentExemption"), // TODO
        GET_MULTIPLE_ACCOUNTS("getMultipleAccounts"),
        GET_PROGRAM_ACCOUNTS("getProgramAccounts"), // TODO
        GET_RECENT_PERFORMANCE_SAMPLES("getRecentPerformanceSamples"),
        GET_SIGNATURES_FOR_ADDRESS("getSignaturesForAddress"), // TODO
        GET_SIGNATURE_STATUSES("getSignatureStatuses"),
        GET_SLOT("getSlot"),
        GET_SLOT_LEADER("getSlotLeader"),
        GET_SLOT_LEADERS("getSlotLeaders"),
        GET_STAKE_ACTIVATION("getStakeActivation"),
        GET_SUPPLY("getSupply"),
        GET_TOKEN_ACCOUNT_BALANCE("getTokenAccountBalance"),
        GET_TOKEN_ACCOUNTS_BY_DELEGATE("getTokenAccountsByDelegate"),
        GET_TOKEN_ACCOUNT_BY_OWNER("getTokenAccountsByOwner"),
        GET_TOKEN_LARGEST_ACCOUNTS("getTokenLargestAccounts"),
        GET_TOKEN_SUPPLY("getTokenSupply"),
        GET_TRANSACTION("getTransaction"),
        GET_TRANSACTION_COUNT("getTransactionCount"),
        GET_VERSION("getVersion"),
        GET_VOTE_ACCOUNTS("getVoteAccounts"),
        IS_BLOCKHASH_VALID("isBlockhashValid"),
        MINIMUM_LEDGER_SLOT("minimumLedgerSlot"),
        REQUEST_AIRDROP("requestAirdrop"), // TODO
        SEND_TRANSACTION("sendTransaction"), // TODO
        SIMULATE_TRANSACTION("simulateTransaction"),

        WEB_SOCKET_ACCOUNT_SUBSCRIBE("accountSubscribe"),
        WEB_SOCKET_ACCOUNT_UNSUBSCRIBE("accountUnsubscribe"),
        WEB_SOCKET_LOGS_SUBSCRIBE("logsSubscribe"),
        WEB_SOCKET_LOGS_UNSUBSCRIBE("logsUnsubscribe"),
        WEB_SOCKET_PROGRAM_SUBSCRIBE("programSubscribe"),
        WEB_SOCKET_PROGRAM_UNSUBSCRIBE("programUnsubscribe"),
        WEB_SOCKET_SIGNATURE_SUBSCRIBE("signatureSubscribe"),
        WEB_SOCKET_SIGNATURE_UNSUBSCRIBE("signatureUnsubscribe"),
        WEB_SOCKET_SLOT_SUBSCRIBE("slotSubscribe"),
        WEB_SOCKET_SLOT_UNSUBSCRIBE("slotUnsubscribe");

        String method;

        RPCMethod(String method) {
                this.method = method;
        }

        @Override
        public String toString() {
                return method;
        }
}
