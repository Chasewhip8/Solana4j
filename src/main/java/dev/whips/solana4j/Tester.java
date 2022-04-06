package dev.whips.solana4j;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.exceptions.RPCException;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.data.PubKey;
import dev.whips.solana4j.programs.RaydiumAMMV4;
import dev.whips.solana4j.programs.SPLTokenAccount;
import dev.whips.solana4j.programs.SerumOpenOrdersV2;
import dev.whips.solana4j.utils.DataReader;

public class Tester {
    public static void main(String[] args) throws RPCException {
        SolanaAPI solanaAPI = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.MAINNET_BETA)
                .build();
        //27EMreeQQabEwbuyLPYxz3GXEKirP6LtHVJcY3QvYvoM

        PubKey wallet = new PubKey("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT");
        PubKey JFI_USDC_AMM_Market = new PubKey("8Skw2e6PeEvyoMGXsKAk4TLM86Qh29zRQYXzXEzxRm8Y");

        AccountInfo accountInfo = solanaAPI.getAccountInfo(JFI_USDC_AMM_Market, RPCEncoding.BASE64);
        DataReader dataReader = accountInfo.getDataReader();
        RaydiumAMMV4 raydiumAMM = new RaydiumAMMV4(dataReader);
        int baseDecimals = raydiumAMM.getBase_decimal().intValue();
        int quoteDecimals = raydiumAMM.getQuote_decimal().intValue();

        double base = toDecimal(new SPLTokenAccount(
                solanaAPI.getAccountInfo(raydiumAMM.getBase_vault(), RPCEncoding.BASE64).getDataReader()
        ).getAmount().longValue(), baseDecimals);
        double quote = toDecimal(new SPLTokenAccount(
                solanaAPI.getAccountInfo(raydiumAMM.getQuote_vault(), RPCEncoding.BASE64).getDataReader()
        ).getAmount().longValue(), quoteDecimals);

        SerumOpenOrdersV2 serumOpenOrders = new SerumOpenOrdersV2(solanaAPI.getAccountInfo(raydiumAMM.getOpen_orders(), RPCEncoding.BASE64).getDataReader());

        double baseTotal = base + toDecimal(serumOpenOrders.getBaseTokenTotal().longValue(), baseDecimals)
                - toDecimal(raydiumAMM.getBase_need_take_pnl().longValue(), baseDecimals);
        double quoteTotal = quote + toDecimal(serumOpenOrders.getQuoteTokenTotal().longValue(), quoteDecimals)
                - toDecimal(raydiumAMM.getQuote_need_take_pnl().longValue(), quoteDecimals);

        System.out.println(quoteTotal / baseTotal);
    }

    private static double toDecimal(long value, int decimals){
        return value * Math.pow(10, -decimals);
    }
}
