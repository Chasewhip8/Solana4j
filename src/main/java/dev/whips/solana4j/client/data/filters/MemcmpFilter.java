package dev.whips.solana4j.client.data.filters;

import com.google.common.primitives.UnsignedLong;
import io.github.novacrypto.base58.Base58;

public class MemcmpFilter implements Filter {
    private UnsignedLong offset;
    private String bytes;

    public MemcmpFilter(UnsignedLong offset, String bytes) {
        this.offset = offset;
        this.bytes = bytes;
    }

    public MemcmpFilter(UnsignedLong offset, byte[] bytes) {
        this.offset = offset;

        if (bytes.length > 129){
            throw new IllegalArgumentException("Invalid byte length for Memcmp, 129 max.");
        }

        this.bytes = Base58.base58Encode(bytes);
    }

    public MemcmpFilter() {

    }

    public UnsignedLong getOffset() {
        return offset;
    }

    public String getBytes() {
        return bytes;
    }
}
