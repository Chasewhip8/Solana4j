package dev.whips.solana4j.client.data.config;

public class ConfirmedSignaturesConfig {
    private long limit;
    private String before;
    private String until;

    public ConfirmedSignaturesConfig(int limit) {
        this.limit = limit;
    }

    public ConfirmedSignaturesConfig() {

    }

    public long getLimit() {
        return limit;
    }

    public String getBefore() {
        return before;
    }

    public String getUntil() {
        return until;
    }
}
