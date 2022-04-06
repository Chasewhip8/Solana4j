package dev.whips.solana4j.client;

import dev.whips.solana4j.client.data.SolanaError;

public class RPCResponse<T> {
    private String jsonrpc;
    private int id;
    private SolanaError error;
    private T result;

    public RPCResponse(String jsonrpc, T result, int id, SolanaError error) {
        this.jsonrpc = jsonrpc;
        this.result = result;
        this.id = id;
        this.error = error;
    }

    public RPCResponse() {

    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public int getId() {
        return id;
    }

    public SolanaError getError() {
        return error;
    }
}
