package dev.whips.solana4j.client;

import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.results.ContextResult;

public class RPCNotification {
    public String jsonrpc;
    public String method;
    public Params params;

    public RPCNotification(String jsonrpc, String method, Params params) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
    }

    public RPCNotification() {

    }

    public static class Params {
        private ContextResult<AccountInfo> result;
        private long subscription;

        public Params(ContextResult<AccountInfo> result, long subscription) {
            this.result = result;
            this.subscription = subscription;
        }

        public Params() {

        }

        public ContextResult<AccountInfo> getResult() {
            return result;
        }

        public long getSubscription() {
            return subscription;
        }
    }
}
