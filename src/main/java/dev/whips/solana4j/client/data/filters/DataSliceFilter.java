package dev.whips.solana4j.client.data.filters;

public class DataSliceFilter {
    private int offset;
    private int length;

    public DataSliceFilter(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public DataSliceFilter() {

    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
