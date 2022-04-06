package dev.whips.solana4j.client.data;

public class SolanaError {
    private long code;
    private String message;

    public SolanaError(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public SolanaError() {

    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
