package dev.whips.solana4j.client;

import java.util.List;

public class RPCRequest {
    public String jsonrpc;
    public int id;
    public String method;
    public List<Object> params;

    public RPCRequest(String jsonrpc, int id, String method, List<Object> params) {
        this.jsonrpc = jsonrpc;
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public RPCRequest() {

    }
}
