package dev.whips.solana4j.client.data;

import com.google.common.primitives.UnsignedLong;

public class SignatureInformation {
    private String signature;
    private UnsignedLong slot;
    private SolanaError err;
    private String memo;
    private long blockTime;

    public SignatureInformation(String signature, UnsignedLong slot, SolanaError err, String memo, long blockTime) {
        this.signature = signature;
        this.slot = slot;
        this.err = err;
        this.memo = memo;
        this.blockTime = blockTime;
    }

    public SignatureInformation() {

    }

    public String getSignature() {
        return signature;
    }

    public UnsignedLong getSlot() {
        return slot;
    }

    public SolanaError getErr() {
        return err;
    }

    public String getMemo() {
        return memo;
    }

    public long getBlockTime() {
        return blockTime;
    }
}
