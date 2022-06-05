package dev.whips.solana4j.client.websocket;

public class Subscription<T> {
    private final RPCSubscription<T> subscription;
    private final NotificationListener<RPCNotification<T>> listener;

    public Subscription(RPCSubscription<T> subscription, NotificationListener<RPCNotification<T>> listener) {
        this.subscription = subscription;
        this.listener = listener;
    }

    public RPCSubscription<T> getSubscription() {
        return subscription;
    }

    public NotificationListener<RPCNotification<T>> getListener() {
        return listener;
    }
}
