package dev.whips.solana4j.client.data.filters;

import io.github.novacrypto.base58.Base58;

public class MemcmpFilter implements Filter {
    private Memcmp memcmp;

    public MemcmpFilter(int offset, String bytes) {
        this.memcmp = new Memcmp(offset, bytes);
    }

    public MemcmpFilter(int offset, byte[] bytes) {
        if (bytes.length > 129){
            throw new IllegalArgumentException("Invalid byte length for Memcmp, 129 max.");
        }

        this.memcmp = new Memcmp(offset, Base58.base58Encode(bytes));
    }

    public MemcmpFilter() {

    }

    public Memcmp getMemcmp() {
        return memcmp;
    }

    public static class Memcmp {
        private int offset;
        private String bytes;

        public Memcmp(int offset, String bytes) {
            this.offset = offset;
            this.bytes = bytes;
        }

        public Memcmp() {

        }

        public int getOffset() {
            return offset;
        }

        public String getBytes() {
            return bytes;
        }
    }
}
