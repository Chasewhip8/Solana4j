package dev.whips.solana4j.client.data;

public class SlotInfo {
    private long parent;
    private long root;
    private long slot;

    public SlotInfo(long parent, long root, long slot) {
        this.parent = parent;
        this.root = root;
        this.slot = slot;
    }

    public SlotInfo() {

    }

    public long getParent() {
        return parent;
    }

    public long getRoot() {
        return root;
    }

    public long getSlot() {
        return slot;
    }
}
