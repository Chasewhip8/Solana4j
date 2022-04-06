package dev.whips.solana4j.client.data;

public class ProgramAccount {
    private AccountInfo account;
    private String pubkey;

    public ProgramAccount(AccountInfo account, String pubkey) {
        this.account = account;
        this.pubkey = pubkey;
    }

    public ProgramAccount() {

    }

    public AccountInfo getAccount() {
        return account;
    }

    public String getPubkey() {
        return pubkey;
    }
}
