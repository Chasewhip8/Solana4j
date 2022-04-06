package dev.whips.solana4j.client.data;

public class RecentBlockhash {
    private String blockhash;
    private FeeCalculator feeCalculator;

    public RecentBlockhash(String blockhash, FeeCalculator feeCalculator) {
        this.blockhash = blockhash;
        this.feeCalculator = feeCalculator;
    }

    public RecentBlockhash() {

    }

    public String getBlockhash() {
        return blockhash;
    }

    public FeeCalculator getFeeCalculator() {
        return feeCalculator;
    }

    public static class FeeCalculator {
        private long lamportsPerSignature;

        public FeeCalculator(long lamportsPerSignature) {
            this.lamportsPerSignature = lamportsPerSignature;
        }

        public FeeCalculator() {

        }

        public long getLamportsPerSignature() {
            return lamportsPerSignature;
        }
    }
}
