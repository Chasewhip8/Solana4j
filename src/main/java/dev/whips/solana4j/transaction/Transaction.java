package dev.whips.solana4j.transaction;

import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.utils.ByteSerializable;
import dev.whips.solana4j.utils.CompactArray;
import dev.whips.solana4j.utils.AccountArrayMerger;
import org.checkerframework.checker.units.qual.A;

import java.nio.ByteBuffer;
import java.util.*;

public class Transaction implements ByteSerializable {
    private final List<Instruction> instructions;

    //Header Data
    private static final int HEADER_LENGTH = 3;

    private static final int BLOCKHASH_LENGTH = 32;
    private byte[] recentBlockHash;

    /*
        Transaction Format
            CompactArray<Signature> - 64 bytes * length
            Message

        Message Format
            requiredSignatures - u8
            amountReadOnly - u8
            amountReadOnlyNoSign - u8

            CompactArray<Account Addresses>
                With Signature Read-Write Accounts
                With Signature Read-Only Accounts
                Read-Write Accounts
                Read-Only Accounts

            blockhash - 32 byte
            CompactArray<Instruction>
     */

    public Transaction() {
        this.instructions = new ArrayList<>();
    }

    public void addInstruction(Instruction... newInstruction){
        this.instructions.addAll(List.of(newInstruction));
    }

    public byte[] getRecentBlockHash() {
        return recentBlockHash;
    }

    public void setRecentBlockHash(byte[] recentBlockHash) {
        this.recentBlockHash = recentBlockHash;
    }

    @Override
    public byte[] serialize() {
        AccountArrayMerger sortedAccounts = new AccountArrayMerger();
        for (Instruction instruction : instructions){
            sortedAccounts.addAll(instruction.getRequiredAccounts());
        }
        ArrayList<Account> sortedAccountsList = new ArrayList<>(List.of((Account[]) sortedAccounts.toArray()));

        ByteBuffer messageBuffer = ByteBuffer.allocate(1000);

        // Message Header
        int requireSignatures = 0;
        int amountReadOnly = 0;
        int amountNoSign = 0;

        for (Account account : sortedAccounts){
            if (account.requiresSignature()){
                requireSignatures++;
            } else {
                amountNoSign++;
            }
            if (!account.isWriteable()){
                amountReadOnly++;
            }
        }

        ArrayList<Byte[]> compiledInstructions = new ArrayList<>(instructions.size());

        messageBuffer.put((byte) requireSignatures);
        messageBuffer.put((byte) amountReadOnly);
        messageBuffer.put((byte) amountNoSign);

        // Accounts Array
        messageBuffer.put(sortedAccounts.serialize());

        // Recent Blockhash
        messageBuffer.put(recentBlockHash);

        // Instructions Array


        return new byte[0];
    }

    @Override
    public int getSerializedLength() {
        return 0;
    }

    private byte[] serializeInstruction(ArrayList<Account> accounts, Instruction instruction){
        return null;
    }

    public class Instruction {
        private final PubKey programId;
        private final Set<Account> requiredAccounts;
        private final ByteBuffer dataBuffer;

        public Instruction(PubKey programId, int dataLength) {
            this.programId = programId;
            this.requiredAccounts = new HashSet<>();
            this.dataBuffer = ByteBuffer.allocate(dataLength);
        }

        public PubKey getProgramId() {
            return programId;
        }

        public Set<Account> getRequiredAccounts() {
            return requiredAccounts;
        }

        public ByteBuffer getDataBuffer() {
            return dataBuffer;
        }
    }
}
