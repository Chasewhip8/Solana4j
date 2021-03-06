package dev.whips.solana4j.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.whips.solana4j.SolanaCluster;
import dev.whips.solana4j.client.providers.JacksonMappingsProvider;
import dev.whips.solana4j.exceptions.RPCException;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

public class RPCClient {
    private static final MediaType mediaType = MediaType.parse("application/json");

    private final SolanaCluster cluster;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public RPCClient(SolanaCluster cluster, int readTimeout) {
        this.cluster = cluster;
        this.client = new OkHttpClient.Builder()
                .readTimeout(Duration.ofMillis(readTimeout))
                .build();
        this.objectMapper = JacksonMappingsProvider.createObjectMapper();
    }

    public <T> T call(RPCRequest requestData, TypeReference<T> typeReference) throws RPCException {
        try {
            byte[] data = objectMapper.writeValueAsBytes(requestData);

            Request request = new Request.Builder()
                    .url(cluster.getEndpoint())
                    .post(RequestBody.create(mediaType, data))
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
        return call(new RPCRequest("2.0", RPCUtils.generateUniqueId(), method.toString(), Arrays.asList(params)), typeReference);
    }

    public <T> T callRaw(RPCMethod method, TypeReference<T> typeReference, Object... params) throws RPCException {
        return call(new RPCRequest("2.0", RPCUtils.generateUniqueId(), method.toString(), Arrays.asList(params)), typeReference);
    }
}
