package dev.whips.solana4j.utils;

public interface ByteSerializable {
    byte[] serialize();

    int getSerializedLength();
}
