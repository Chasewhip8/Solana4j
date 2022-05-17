package dev.whips.solana4j.client;

import java.util.List;

public class RPCRequest {
    private String jsonrpc;
    private int id;
    private String method;
    private List<Object> params;

    public RPCRequest(String jsonrpc, int id, String method, List<Object> params) {
        this.jsonrpc = jsonrpc;
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public RPCRequest() {

    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public int getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public List<Object> getParams() {
        return params;
    }
}
