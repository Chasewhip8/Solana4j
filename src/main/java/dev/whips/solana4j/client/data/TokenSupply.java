package dev.whips.solana4j.client.data;

public class TokenSupply {
    private String amount;
    private int decimals;
    private String uiAmountString;
    private String uiAmount;

    public TokenSupply(String amount, int decimals, String uiAmountString) {
        this.amount = amount;
        this.decimals = decimals;
        this.uiAmountString = uiAmountString;
    }

    public TokenSupply() {

    }

    public String getAmount() {
        return amount;
    }

    public int getDecimals() {
        return decimals;
    }

    public String getUiAmountString() {
        return uiAmountString;
    }

    public String getUiAmount() {
        return uiAmount;
    }
}
