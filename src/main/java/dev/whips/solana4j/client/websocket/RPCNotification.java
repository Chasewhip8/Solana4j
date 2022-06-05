package dev.whips.solana4j.client.websocket;

import dev.whips.solana4j.client.data.results.ContextResult;

public class RPCNotification<T> {
    private String jsonrpc;
    private String method;
    private Params<T> params;

    public RPCNotification(String jsonrpc, String method, Params<T> params) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
    }

    public RPCNotification() {

    }

    public static class Params<T> {
        private T result;
        private long subscription;

        public Params(T result, long subscription) {
            this.result = result;
            this.subscription = subscription;
        }

        public Params() {

        }

        public T getResult() {
            return result;
        }

        public long getSubscription() {
            return subscription;
        }
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public Params<T> getParams() {
        return params;
    }
}
