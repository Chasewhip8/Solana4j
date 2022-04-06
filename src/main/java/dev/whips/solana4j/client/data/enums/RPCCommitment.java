package dev.whips.solana4j.client.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RPCCommitment {
    FINALIZED("finalized"),
    CONFIRMED("confirmed"),
    PROCESSED("processed");

    String commitment;

    RPCCommitment(String commitment){
        this.commitment = commitment;
    }

    @JsonValue
    @Override
    public String toString() {
        return commitment;
    }
}
