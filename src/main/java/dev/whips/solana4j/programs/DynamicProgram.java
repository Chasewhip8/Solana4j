package dev.whips.solana4j.programs;

import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.results.ContextResult;
import dev.whips.solana4j.client.websocket.Subscription;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.utils.DataReader;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DynamicProgram extends BaseProgram implements AutoCloseable {
    private static final int UPDATE_DELAY_MS = 10;

    protected final SolanaAPI api;
    private final Set<ProgramUpdateListener> listeners;
    private final long initializationTime;

    private Subscription<ContextResult<AccountInfo>> subscription;

    public DynamicProgram(SolanaAPI api, PubKey pubKey){
        super(pubKey);

        this.api = api;
        this.listeners = new HashSet<>();
        this.initializationTime = System.currentTimeMillis();

        try {
            updateProgramData(api.getAccountInfo(pubKey, RPCEncoding.BASE64).getDataReader());
        } catch (RPCException | ContractException e){
            throw new RuntimeException("Invalid contract data");
        }
    }

    private void checkAndEnableUpdates(){
        if (listeners.size() != 0 || (System.currentTimeMillis() - initializationTime) < UPDATE_DELAY_MS){
            return;
        }

        try {
            subscription = api.subscribeAccount(getPubKey(), RPCEncoding.BASE64, (update) -> {
                try {
                    updateProgramData(update.getParams().getResult().getValue().getDataReader());

                    for (ProgramUpdateListener listener : listeners){
                        listener.programUpdate(update.getParams().getResult().getContext().getSlot());
                    }
                } catch (ContractException e) {
                    throw new RuntimeException("Invalid contract data");
                }
            });
        } catch (RPCException e) {
            e.printStackTrace();
        }
    }

    public void registerUpdateListener(ProgramUpdateListener listener){
        checkAndEnableUpdates();
        this.listeners.add(listener);
    }

    public void registerMultipleUpdateListeners(Collection<ProgramUpdateListener> listeners){
        checkAndEnableUpdates();
        this.listeners.addAll(listeners);
    }

    public Set<ProgramUpdateListener> getListeners() {
        return listeners;
    }

    @Override
    public void close() throws Exception {
        api.unSubscribeAccount(subscription);
        listeners.clear();
    }

    public abstract int getExpectedDataLength();

    public abstract void updateProgramData(DataReader dataReader);

    protected static boolean needsUpdating(BaseProgram program, PubKey pubKey){
        return program == null || !program.isSamePubKey(pubKey);
    }

    protected static <T extends DynamicProgram> T updateAccount(T oldProgram, T newProgram) throws Exception {
        if (oldProgram == null){
            return newProgram;
        }

        System.out.println("UPDATING ACCOUNT NOW");
        newProgram.registerMultipleUpdateListeners(oldProgram.getListeners());
        oldProgram.close();
        return newProgram;
    }
}
