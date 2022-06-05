package dev.whips.solana4j.utils.serialize;

import dev.whips.solana4j.client.data.PubKey;

public class PubKeyDeserializer implements ByteDeserializer<PubKey> {
    @Override
    public int requiredSize() {
        return PubKey.KEY_LENGTH;
    }

    @Override
    public PubKey decodeFromBytes(byte[] bytes, int offset) {
        byte[] data = new byte[requiredSize()];
        System.arraycopy(bytes, offset, data, 0, data.length);
        return new PubKey(data);
    }
}
