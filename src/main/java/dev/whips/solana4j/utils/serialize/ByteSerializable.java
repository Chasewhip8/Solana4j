package dev.whips.solana4j.utils.serialize;

public interface ByteSerializable {
    byte[] serialize();

    int getSerializedLength();
}
