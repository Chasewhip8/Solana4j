package dev.whips.solana4j.data;

import com.fasterxml.jackson.annotation.JsonValue;
import io.github.novacrypto.base58.Base58;

import java.util.Arrays;

public class PubKey {
    private static final int KEY_LENGTH = 32;

    private final byte[] key;
    private final String stringKey;

    public PubKey(String base58PubKey){
        if (base58PubKey.length() < KEY_LENGTH){
            throw new IllegalArgumentException("Public key is incorrect size.");
        }

        this.stringKey = base58PubKey;
        this.key = Base58.base58Decode(base58PubKey);
    }

    public PubKey(byte[] rawKey){
        if (rawKey.length > KEY_LENGTH){
            throw new IllegalArgumentException("Public key is incorrect size.");
        }

        this.stringKey = Base58.base58Encode(rawKey);
        this.key = rawKey;
    }

    public byte[] getRawKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }

        if (obj instanceof PubKey){
            return obj.hashCode() == this.hashCode();
        }
        return super.equals(obj);
    }

    @JsonValue
    @Override
    public String toString() {
        return stringKey;
    }
}
