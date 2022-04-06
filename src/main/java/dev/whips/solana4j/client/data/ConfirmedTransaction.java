package dev.whips.solana4j.client.data;

import com.google.common.primitives.UnsignedLong;

import java.util.List;

//TODO fix types
public class ConfirmedTransaction {
    private Meta meta;
    private UnsignedLong slot;
    private Transaction transaction;
    private long blockTime;

    public ConfirmedTransaction(Meta meta, UnsignedLong slot, Transaction transaction, long blockTime) {
        this.meta = meta;
        this.slot = slot;
        this.transaction = transaction;
        this.blockTime = blockTime;
    }

    public ConfirmedTransaction() {

    }

    public Meta getMeta() {
        return meta;
    }

    public UnsignedLong getSlot() {
        return slot;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public long getBlockTime() {
        return blockTime;
    }

    public static class Header {
        private long numReadonlySignedAccounts;
        private long numReadonlyUnsignedAccounts;
        private long numRequiredSignatures;

        public Header(long numReadonlySignedAccounts, long numReadonlyUnsignedAccounts, long numRequiredSignatures) {
            this.numReadonlySignedAccounts = numReadonlySignedAccounts;
            this.numReadonlyUnsignedAccounts = numReadonlyUnsignedAccounts;
            this.numRequiredSignatures = numRequiredSignatures;
        }

        public Header() {

        }

        public long getNumReadonlySignedAccounts() {
            return numReadonlySignedAccounts;
        }

        public long getNumReadonlyUnsignedAccounts() {
            return numReadonlyUnsignedAccounts;
        }

        public long getNumRequiredSignatures() {
            return numRequiredSignatures;
        }
    }

    public static class Instruction {
        private List<Long> accounts;
        private String data;
        private long programIdIndex;

        public Instruction(List<Long> accounts, String data, long programIdIndex) {
            this.accounts = accounts;
            this.data = data;
            this.programIdIndex = programIdIndex;
        }

        public Instruction() {

        }

        public List<Long> getAccounts() {
            return accounts;
        }

        public String getData() {
            return data;
        }

        public long getProgramIdIndex() {
            return programIdIndex;
        }
    }

    public static class Message {
        private List<String> accountKeys;
        private Header header;
        private List<Instruction> instructions;
        private String recentBlockhash;

        public Message(List<String> accountKeys, Header header, List<Instruction> instructions, String recentBlockhash) {
            this.accountKeys = accountKeys;
            this.header = header;
            this.instructions = instructions;
            this.recentBlockhash = recentBlockhash;
        }

        public Message() {

        }

        public List<String> getAccountKeys() {
            return accountKeys;
        }

        public Header getHeader() {
            return header;
        }

        public List<Instruction> getInstructions() {
            return instructions;
        }

        public String getRecentBlockhash() {
            return recentBlockhash;
        }
    }

    public static class Status {
        public Object ok;

        public Status(Object ok) {
            this.ok = ok;
        }

        public Status() {

        }
    }

    public static class Meta {
        private Object err;
        private long fee;
        private List<Object> innerInstructions;
        private List<Long> postBalances;
        private List<Long> preBalances;
        private Status status;

        public Meta(Object err, long fee, List<Object> innerInstructions, List<Long> postBalances, List<Long> preBalances, Status status) {
            this.err = err;
            this.fee = fee;
            this.innerInstructions = innerInstructions;
            this.postBalances = postBalances;
            this.preBalances = preBalances;
            this.status = status;
        }

        public Meta() {

        }

        public Object getErr() {
            return err;
        }

        public long getFee() {
            return fee;
        }

        public List<Object> getInnerInstructions() {
            return innerInstructions;
        }

        public List<Long> getPostBalances() {
            return postBalances;
        }

        public List<Long> getPreBalances() {
            return preBalances;
        }

        public Status getStatus() {
            return status;
        }
    }

    public static class Transaction {
        private Message message;
        private List<String> signatures;

        public Transaction(Message message, List<String> signatures) {
            this.message = message;
            this.signatures = signatures;
        }

        public Transaction() {

        }

        public Message getMessage() {
            return message;
        }

        public List<String> getSignatures() {
            return signatures;
        }
    }
}
