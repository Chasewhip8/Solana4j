package dev.whips.solana4j.client.data.results;

public class ContextResult<T> {
    private Context context;
    private T value;

    public ContextResult(Context context, T value) {
        this.context = context;
        this.value = value;
    }

    public ContextResult() {

    }

    public Context getContext() {
        return context;
    }

    public T getValue() {
        return value;
    }

    public static class Context {
        private long slot;

        public Context(long slot) {
            this.slot = slot;
        }

        public Context() {

        }

        public long getSlot() {
            return slot;
        }
    }
}
