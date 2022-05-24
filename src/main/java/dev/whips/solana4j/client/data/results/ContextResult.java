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
        private String apiVersion;

        public Context(long slot, String apiVersion) {
            this.slot = slot;
            this.apiVersion = apiVersion;
        }

        public Context() {

        }

        public long getSlot() {
            return slot;
        }

        public String getApiVersion() {
            return apiVersion;
        }
    }
}
