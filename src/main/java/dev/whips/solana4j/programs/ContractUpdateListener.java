package dev.whips.solana4j.programs;

public interface ContractUpdateListener<T> {
    void contractUpdate(T data);
}
