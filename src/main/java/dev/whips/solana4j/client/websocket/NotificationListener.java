package dev.whips.solana4j.client.websocket;

public interface NotificationListener<T> {
    void call(T data);
}
