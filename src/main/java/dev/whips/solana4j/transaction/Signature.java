package dev.whips.solana4j.transaction;

import dev.whips.solana4j.utils.ByteSerializable;

public class Signature implements ByteSerializable {
    private final int SIGNATURE_LENGTH = 64;

    @Override
    public byte[] serialize() {
        return new byte[SIGNATURE_LENGTH];
    }

    @Override
    public int getSerializedLength() {
        return SIGNATURE_LENGTH;
    }
}
