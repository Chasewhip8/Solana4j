package dev.whips.solana4j.programs.layout;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.programs.BaseProgram;
import dev.whips.solana4j.utils.DataReader;

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
