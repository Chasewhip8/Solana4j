package dev.whips.solana4j.client.data;

public class ProgramAccount {
    private AccountInfo account;
    private PubKey pubkey;

    public ProgramAccount(AccountInfo account, PubKey pubkey) {
        this.account = account;
        this.pubkey = pubkey;
    }

    public ProgramAccount() {

    }

    public AccountInfo getAccount() {
        return account;
    }

    public PubKey getPubkey() {
        return pubkey;
    }
}
