package dev.whips.solana4j.programs.layout;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.client.data.TokenBalance;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.programs.BaseProgram;
import dev.whips.solana4j.utils.DataReader;

import java.math.BigInteger;

public class SaberStableSwap extends BaseProgram {
    private final SolanaAPI api;
    private final PubKey contractKey;

    // U8
    private final long isInitialized;
    private final long isPaused;
    private final long nonce;

    // U64
    private final UnsignedLong initialAmpFactor;
    private final UnsignedLong targetAmpFactor;

    // ns64 - U64?
    private final UnsignedLong startRampTs;
    private final UnsignedLong stopRampTs;
    private final UnsignedLong futureAdminDeadline;

    // PubKey
    private final PubKey futureAdminAccount;
    private final PubKey adminAccount;
    private final PubKey tokenAccountA;
    private final PubKey tokenAccountB;
    private final PubKey tokenPool;
    private final PubKey mintA;
    private final PubKey mintB;
    private final PubKey adminFeeAccountA;
    private final PubKey adminFeeAccountB;

    // Fees Layout
    private final FeesLayout feesLayout;

    public SaberStableSwap(SolanaAPI api, PubKey contractKey) throws RPCException, ContractException {
        this.api = api;
        this.contractKey = contractKey;

        AccountInfo accountInfo = api.getAccountInfo(contractKey, RPCEncoding.BASE64);
        DataReader dataReader = accountInfo.getDataReader();

        this.isInitialized = dataReader.readU8();
        this.isPaused = dataReader.readU8();
        this.nonce = dataReader.readU8();

        this.initialAmpFactor = dataReader.readU64();
        this.targetAmpFactor = dataReader.readU64();

        this.startRampTs = dataReader.readU64();
        this.stopRampTs = dataReader.readU64();
        this.futureAdminDeadline = dataReader.readU64();

        this.futureAdminAccount = dataReader.readPubKey();
        this.adminAccount = dataReader.readPubKey();
        this.tokenAccountA = dataReader.readPubKey();
        this.tokenAccountB = dataReader.readPubKey();
        this.tokenPool = dataReader.readPubKey();
        this.mintA = dataReader.readPubKey();
        this.mintB = dataReader.readPubKey();
        this.adminFeeAccountA = dataReader.readPubKey();
        this.adminFeeAccountB = dataReader.readPubKey();

        this.feesLayout = new FeesLayout(dataReader);
    }

    public double requestRawSwapPriceRatio() throws RPCException {
        TokenBalance tokenAReserveBalance = api.getTokenAccountBalance(tokenAccountA).getValue();
        TokenBalance tokenBReserveBalance = api.getTokenAccountBalance(tokenAccountB).getValue();

        long inputAmountNum = (long) Math.max(10_000,
                Math.min(
                        Math.pow(10, tokenAReserveBalance.getDecimals()),
                        Math.floor((double) Long.parseLong(tokenAReserveBalance.getAmount()) / 100)
                ));

        if (inputAmountNum == 0){
            return 0;
        }

        long fromReservesRaw = Long.parseLong(tokenAReserveBalance.getAmount());
        long toReservesRaw = Long.parseLong(tokenBReserveBalance.getAmount());
        long ampFactor = getAmpFactor();

        BigInteger outputAmount = BigInteger.valueOf(toReservesRaw).subtract(
                computeY(ampFactor, fromReservesRaw + inputAmountNum,
                computeD(ampFactor, fromReservesRaw, toReservesRaw)));

        return (double) outputAmount.longValue() / inputAmountNum;
    }

    public static BigInteger computeD(long ampFactor, long amountA, long amountB){
        long S = amountA + amountB; // sum(x_i), a.k.a S
        if (S == 0) {
            return BigInteger.ZERO;
        }

        long ann = ampFactor * 2; // A*n^n

        BigInteger dPrev = BigInteger.ZERO;
        BigInteger d = BigInteger.valueOf(S);
        for (int i = 0; d.subtract(dPrev).abs().compareTo(BigInteger.ONE) > 0 && i < 20; i++){
            dPrev = d;
            BigInteger dP = d;
            dP = dP.multiply(d).divide(BigInteger.valueOf(amountA * 2));
            dP = dP.multiply(d).divide(BigInteger.valueOf(amountB * 2));
            BigInteger dNumerator = d.multiply(dP.multiply(BigInteger.TWO).add(BigInteger.valueOf(ann * S)));
            BigInteger dDenominator = d.multiply(BigInteger.valueOf(ann - 1)).add(dP.multiply(BigInteger.valueOf(3)));
            d = dNumerator.divide(dDenominator);
        }

        return d;
    }

    public static BigInteger computeY(long ampFactor, long x, BigInteger d){
        BigInteger ann = BigInteger.valueOf(ampFactor * 2); // A*n^n
        // sum' = prod' = x
        BigInteger b = BigInteger.valueOf(x).add(d.divide(ann)).subtract(d); // b = sum' - (A*n**n - 1) * D / (A * n**n)
        // c =  D ** (n + 1) / (n ** (2 * n) * prod' * A)
        BigInteger c = d.pow(3).divide(BigInteger.valueOf(4 * x).multiply(ann));

        BigInteger yPrev = BigInteger.ZERO;
        BigInteger y = d;
        for (int i = 0; i < 20 && y.subtract(yPrev).abs().compareTo(BigInteger.ONE) > 0; i++) {
            yPrev = y;
            y = ((y.multiply(y)).add(c)).divide(y.multiply(BigInteger.TWO).add(b));
        }

        return y;
    }

    public long getAmpFactor(){
        long now = System.currentTimeMillis() / 1_000;

        // The most common case is that there is no ramp in progress.
        if (now >= stopRampTs.longValue()) {
            return targetAmpFactor.longValue();
        }

        // If the ramp is about to start, use the initial amp.
        if (now <= startRampTs.longValue()) {
            return initialAmpFactor.longValue();
        }

        long initialAmpFactorLong = initialAmpFactor.longValue();

        // Calculate how far we are along the ramp curve.
        float percent = now >= stopRampTs.longValue() ? 1 : now <= startRampTs.longValue() ? 0 : (now - startRampTs.longValue()) / (stopRampTs.longValue() - startRampTs.longValue());
        long diff = (long) Math.floor((targetAmpFactor.longValue() - initialAmpFactorLong) * percent);

        return initialAmpFactorLong + diff;
    }

    public static class FeesLayout {
        private final UnsignedLong adminTradeFeeNumerator;
        private final UnsignedLong adminTradeFeeDenominator;
        private final UnsignedLong adminWithdrawFeeNumerator;
        private final UnsignedLong adminWithdrawFeeDenominator;
        private final UnsignedLong tradeFeeNumerator;
        private final UnsignedLong tradeFeeDenominator;
        private final UnsignedLong withdrawFeeNumerator;
        private final UnsignedLong withdrawFeeDenominator;

        public FeesLayout(DataReader dataReader) throws RPCException {
            checkProgramSize(dataReader, 64);

            this.adminTradeFeeNumerator = dataReader.readU64();
            this.adminTradeFeeDenominator = dataReader.readU64();
            this.adminWithdrawFeeNumerator = dataReader.readU64();
            this.adminWithdrawFeeDenominator = dataReader.readU64();
            this.tradeFeeNumerator = dataReader.readU64();
            this.tradeFeeDenominator = dataReader.readU64();
            this.withdrawFeeNumerator = dataReader.readU64();
            this.withdrawFeeDenominator = dataReader.readU64();
        }

        public UnsignedLong getAdminTradeFeeNumerator() {
            return adminTradeFeeNumerator;
        }

        public UnsignedLong getAdminTradeFeeDenominator() {
            return adminTradeFeeDenominator;
        }

        public UnsignedLong getAdminWithdrawFeeNumerator() {
            return adminWithdrawFeeNumerator;
        }

        public UnsignedLong getAdminWithdrawFeeDenominator() {
            return adminWithdrawFeeDenominator;
        }

        public UnsignedLong getTradeFeeNumerator() {
            return tradeFeeNumerator;
        }

        public UnsignedLong getTradeFeeDenominator() {
            return tradeFeeDenominator;
        }

        public UnsignedLong getWithdrawFeeNumerator() {
            return withdrawFeeNumerator;
        }

        public UnsignedLong getWithdrawFeeDenominator() {
            return withdrawFeeDenominator;
        }
    }

    public SolanaAPI getApi() {
        return api;
    }

    public PubKey getContractKey() {
        return contractKey;
    }

    public long getIsInitialized() {
        return isInitialized;
    }

    public long getIsPaused() {
        return isPaused;
    }

    public long getNonce() {
        return nonce;
    }

    public UnsignedLong getInitialAmpFactor() {
        return initialAmpFactor;
    }

    public UnsignedLong getTargetAmpFactor() {
        return targetAmpFactor;
    }

    public UnsignedLong getStartRampTs() {
        return startRampTs;
    }

    public UnsignedLong getStopRampTs() {
        return stopRampTs;
    }

    public UnsignedLong getFutureAdminDeadline() {
        return futureAdminDeadline;
    }

    public PubKey getFutureAdminAccount() {
        return futureAdminAccount;
    }

    public PubKey getAdminAccount() {
        return adminAccount;
    }

    public PubKey getTokenAccountA() {
        return tokenAccountA;
    }

    public PubKey getTokenAccountB() {
        return tokenAccountB;
    }

    public PubKey getTokenPool() {
        return tokenPool;
    }

    public PubKey getMintA() {
        return mintA;
    }

    public PubKey getMintB() {
        return mintB;
    }

    public PubKey getAdminFeeAccountA() {
        return adminFeeAccountA;
    }

    public PubKey getAdminFeeAccountB() {
        return adminFeeAccountB;
    }

    public FeesLayout getFeesLayout() {
        return feesLayout;
    }
}
