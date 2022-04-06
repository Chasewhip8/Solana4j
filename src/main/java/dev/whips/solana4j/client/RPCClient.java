package dev.whips.solana4j.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whips.solana4j.SolanaCluster;
import dev.whips.solana4j.client.exceptions.RPCException;
import dev.whips.solana4j.data.providers.JacksonMappingsProvider;
import okhttp3.*;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class RPCClient {
    private static final MediaType mediaType = MediaType.parse("application/json");

    private final SolanaCluster cluster;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final int identifier;

    public RPCClient(SolanaCluster cluster) {
        this.cluster = cluster;
        this.client = new OkHttpClient();
        this.objectMapper = JacksonMappingsProvider.createObjectMapper();
        this.identifier = Math.abs(new Random().nextInt()); // Generate a random uuid for the client
    }

    public <T> T call(RPCRequest requestData, TypeReference<T> typeReference) throws RPCException {
        try {
            String data = objectMapper.writeValueAsString(requestData);

            Request request = new Request.Builder()
                    .url(cluster.getEndpoint())
                    .post(RequestBody.create(data, mediaType))
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            if (response.isSuccessful() && body != null) {
                return objectMapper.readValue(body.byteStream(), typeReference);
            }
        } catch (IOException e) {
            throw new RPCException(e.getMessage());
        }

        return null;
    }

    public <T> RPCResponse<T> call(RPCMethod method, TypeReference<RPCResponse<T>> typeReference, Object... params) throws RPCException {
        return call(new RPCRequest("2.0", identifier, method.toString(), Arrays.asList(params)), typeReference);
    }

    public <T> T callRaw(RPCMethod method, TypeReference<T> typeReference, Object... params) throws RPCException {
        return call(new RPCRequest("2.0", identifier, method.toString(), Arrays.asList(params)), typeReference);
    }
}
