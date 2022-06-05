package dev.whips.solana4j.programs;

import java.util.HashSet;
import java.util.Set;

public class UpdatePropagator<T> {
    private final Set<ContractUpdateListener<T>> listeners;
    private final boolean sendDuplicates;
    private final long propagateLastTimeDelayMillis;

    private long propagationDataSlot;
    private T propagationData;
    private long lastPropagationTime;
    private boolean wasForcePropagated;

    public UpdatePropagator(boolean sendDuplicates, long propagateLastTimeDelayMillis) {
        this.listeners = new HashSet<>();
        this.sendDuplicates = sendDuplicates;
        this.propagateLastTimeDelayMillis = propagateLastTimeDelayMillis;
    }

    public UpdatePropagator() {
        this.listeners = new HashSet<>();
        this.sendDuplicates = false;
        this.propagateLastTimeDelayMillis = 25; // 50ms
    }

    public void propagateUpdate(long slot, T data){
        if (!sendDuplicates && data.equals(propagationData)) {
            return;
        }

        if (propagationData != null && slot > propagationDataSlot && !wasForcePropagated){
            forcePropagation(propagationData);
        }

        this.propagationData = data;
        this.propagationDataSlot = slot;
        this.wasForcePropagated = false;
        this.lastPropagationTime = System.currentTimeMillis();
    }

    public void checkWaitTimeAndPropagate(){
        if (propagationData == null || wasForcePropagated || System.currentTimeMillis() - lastPropagationTime < propagateLastTimeDelayMillis){
            return;
        }

        forcePropagation(propagationData);

        this.wasForcePropagated = true;
        this.lastPropagationTime = System.currentTimeMillis();
    }

    public void forcePropagation(T data){
        for (ContractUpdateListener<T> listener : listeners){
            listener.contractUpdate(data);
        }
    }

    public void addListener(ContractUpdateListener<T> listener){
        listeners.add(listener);
    }

    public void removeListener(ContractUpdateListener<T> listener){
        listeners.remove(listener);
    }

    public void clearListeners(){
        listeners.clear();
    }
}
