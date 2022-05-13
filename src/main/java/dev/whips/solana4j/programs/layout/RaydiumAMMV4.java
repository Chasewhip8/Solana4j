package dev.whips.solana4j.programs.layout;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.AccountInfo;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.BaseProgram;
import dev.whips.solana4j.utils.DataReader;
import dev.whips.solana4j.utils.DataUtils;

import java.math.BigInteger;

public class RaydiumAMMV4 extends BaseProgram {
    private final UnsignedLong status;
    private final UnsignedLong nonce;
    private final UnsignedLong max_order;
    private final UnsignedLong depth;
    /// minimal decimal step amid orders in relation to decimals of relevant mint
    private final UnsignedLong base_decimal;
    private final UnsignedLong quote_decimal;
    private final UnsignedLong state;
    private final UnsignedLong reset_flag;
    /// min size of trade in quote
    private final UnsignedLong min_size;
    private final UnsignedLong vol_max_cut_ratio;
    private final UnsignedLong amount_wave_ratio;
    private final UnsignedLong base_lot_size;
    private final UnsignedLong quote_lot_size;
    private final UnsignedLong min_price_multiplier;
    private final UnsignedLong max_price_multiplier;
    private final UnsignedLong system_decimal_value;
    private final UnsignedLong min_separate_numerator;
    private final UnsignedLong min_separate_denominator;
    private final UnsignedLong trade_fee_numerator;
    private final UnsignedLong trade_fee_denominator;
    private final UnsignedLong pnl_numerator;
    private final UnsignedLong pnl_denominator;
    private final UnsignedLong swap_fee_numerator;
    private final UnsignedLong swap_fee_denominator;
    private final UnsignedLong base_need_take_pnl;
    private final UnsignedLong quote_need_take_pnl;
    /// accrued not yet withdraw fee of quote
    private final UnsignedLong quote_total_pnl;
    /// accrued not yet withdraw fee of base
    private final UnsignedLong base_total_pnl;
    private final BigInteger quote_total_deposited;
    private final BigInteger base_total_deposited;
    private final BigInteger swap_base_in_amount;
    private final BigInteger swap_quote_out_amount;
    // total fee accrued
    private final UnsignedLong swap_base2_quote_fee;
    private final BigInteger swap_quote_in_amount;
    private final BigInteger swap_base_out_amount;
    // total fee accrued
    private final UnsignedLong swap_quote2_base_fee;
    // amm vault
    /// base spl token account
    private final PubKey base_vault;
    /// quite spl token account
    private final PubKey quote_vault;
    // mint
    private final PubKey base_mint;
    private final PubKey quote_mint;
    private final PubKey lp_mint;
    // market
    /// orders on market done by this pool
    private final PubKey open_orders;
    /// usually order book, usually serum
    private final PubKey market_id;
    private final PubKey market_program_id;
    private final PubKey target_orders;
    private final PubKey withdraw_queue;
    private final PubKey lp_vault;
    private final PubKey owner;
    private final PubKey pnl_owner;

    private final SolanaAPI api;

    public RaydiumAMMV4(SolanaAPI api, PubKey pubKey) throws RPCException, ContractException {
        this.api = api;

        AccountInfo accountInfo = api.getAccountInfo(pubKey, RPCEncoding.BASE64);
        DataReader dataReader = accountInfo.getDataReader();

        checkProgramSize(dataReader, 752);
        
        this.status = dataReader.readU64();
        this.nonce = dataReader.readU64();
        this.max_order = dataReader.readU64();
        this.depth = dataReader.readU64();
        this.base_decimal = dataReader.readU64();
        this.quote_decimal = dataReader.readU64();
        this.state = dataReader.readU64();
        this.reset_flag = dataReader.readU64();
        this.min_size = dataReader.readU64();
        this.vol_max_cut_ratio = dataReader.readU64();
        this.amount_wave_ratio = dataReader.readU64();
        this.base_lot_size = dataReader.readU64();
        this.quote_lot_size = dataReader.readU64();
        this.min_price_multiplier = dataReader.readU64();
        this.max_price_multiplier = dataReader.readU64();
        this.system_decimal_value = dataReader.readU64();
        this.min_separate_numerator = dataReader.readU64();
        this.min_separate_denominator = dataReader.readU64();
        this.trade_fee_numerator = dataReader.readU64();
        this.trade_fee_denominator = dataReader.readU64();
        this.pnl_numerator = dataReader.readU64();
        this.pnl_denominator = dataReader.readU64();
        this.swap_fee_numerator = dataReader.readU64();
        this.swap_fee_denominator = dataReader.readU64();
        this.base_need_take_pnl = dataReader.readU64();
        this.quote_need_take_pnl = dataReader.readU64();
        this.quote_total_pnl = dataReader.readU64();
        this.base_total_pnl = dataReader.readU64();
        this.quote_total_deposited = dataReader.readU128();
        this.base_total_deposited = dataReader.readU128();
        this.swap_base_in_amount = dataReader.readU128();
        this.swap_quote_out_amount = dataReader.readU128();
        this.swap_base2_quote_fee = dataReader.readU64();
        this.swap_quote_in_amount = dataReader.readU128();
        this.swap_base_out_amount = dataReader.readU128();
        this.swap_quote2_base_fee = dataReader.readU64();
        this.base_vault = dataReader.readPubKey();
        this.quote_vault = dataReader.readPubKey();
        this.base_mint = dataReader.readPubKey();
        this.quote_mint = dataReader.readPubKey();
        this.lp_mint = dataReader.readPubKey();
        this.open_orders = dataReader.readPubKey();
        this.market_id = dataReader.readPubKey();
        this.market_program_id = dataReader.readPubKey();
        this.target_orders = dataReader.readPubKey();
        this.withdraw_queue = dataReader.readPubKey();
        this.lp_vault = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.pnl_owner = dataReader.readPubKey();
    }

    public double requestCurrentPrice() throws RPCException, ContractException {
        int baseDecimals = getBase_decimal().intValue();
        int quoteDecimals = getQuote_decimal().intValue();

        double base = DataUtils.toDecimal(new SPLTokenAccount(
                api.getAccountInfo(getBase_vault(), RPCEncoding.BASE64).getDataReader()
        ).getAmount().longValue(), baseDecimals);
        double quote = DataUtils.toDecimal(new SPLTokenAccount(
                api.getAccountInfo(getQuote_vault(), RPCEncoding.BASE64).getDataReader()
        ).getAmount().longValue(), quoteDecimals);

        SerumOpenOrdersV2 serumOpenOrders = new SerumOpenOrdersV2(api.getAccountInfo(getOpen_orders(), RPCEncoding.BASE64).getDataReader());

        double baseTotal = base + DataUtils.toDecimal(serumOpenOrders.getBaseTokenTotal().longValue(), baseDecimals)
                - DataUtils.toDecimal(getBase_need_take_pnl().longValue(), baseDecimals);
        double quoteTotal = quote + DataUtils.toDecimal(serumOpenOrders.getQuoteTokenTotal().longValue(), quoteDecimals)
                - DataUtils.toDecimal(getQuote_need_take_pnl().longValue(), quoteDecimals);

        return quoteTotal / baseTotal;
    }

    public UnsignedLong getStatus() {
        return status;
    }

    public UnsignedLong getNonce() {
        return nonce;
    }

    public UnsignedLong getMax_order() {
        return max_order;
    }

    public UnsignedLong getDepth() {
        return depth;
    }

    public UnsignedLong getBase_decimal() {
        return base_decimal;
    }

    public UnsignedLong getQuote_decimal() {
        return quote_decimal;
    }

    public UnsignedLong getState() {
        return state;
    }

    public UnsignedLong getReset_flag() {
        return reset_flag;
    }

    public UnsignedLong getMin_size() {
        return min_size;
    }

    public UnsignedLong getVol_max_cut_ratio() {
        return vol_max_cut_ratio;
    }

    public UnsignedLong getAmount_wave_ratio() {
        return amount_wave_ratio;
    }

    public UnsignedLong getBase_lot_size() {
        return base_lot_size;
    }

    public UnsignedLong getQuote_lot_size() {
        return quote_lot_size;
    }

    public UnsignedLong getMin_price_multiplier() {
        return min_price_multiplier;
    }

    public UnsignedLong getMax_price_multiplier() {
        return max_price_multiplier;
    }

    public UnsignedLong getSystem_decimal_value() {
        return system_decimal_value;
    }

    public UnsignedLong getMin_separate_numerator() {
        return min_separate_numerator;
    }

    public UnsignedLong getMin_separate_denominator() {
        return min_separate_denominator;
    }

    public UnsignedLong getTrade_fee_numerator() {
        return trade_fee_numerator;
    }

    public UnsignedLong getTrade_fee_denominator() {
        return trade_fee_denominator;
    }

    public UnsignedLong getPnl_numerator() {
        return pnl_numerator;
    }

    public UnsignedLong getPnl_denominator() {
        return pnl_denominator;
    }

    public UnsignedLong getSwap_fee_numerator() {
        return swap_fee_numerator;
    }

    public UnsignedLong getSwap_fee_denominator() {
        return swap_fee_denominator;
    }

    public UnsignedLong getBase_need_take_pnl() {
        return base_need_take_pnl;
    }

    public UnsignedLong getQuote_need_take_pnl() {
        return quote_need_take_pnl;
    }

    public UnsignedLong getQuote_total_pnl() {
        return quote_total_pnl;
    }

    public UnsignedLong getBase_total_pnl() {
        return base_total_pnl;
    }

    public BigInteger getQuote_total_deposited() {
        return quote_total_deposited;
    }

    public BigInteger getBase_total_deposited() {
        return base_total_deposited;
    }

    public BigInteger getSwap_base_in_amount() {
        return swap_base_in_amount;
    }

    public BigInteger getSwap_quote_out_amount() {
        return swap_quote_out_amount;
    }

    public UnsignedLong getSwap_base2_quote_fee() {
        return swap_base2_quote_fee;
    }

    public BigInteger getSwap_quote_in_amount() {
        return swap_quote_in_amount;
    }

    public BigInteger getSwap_base_out_amount() {
        return swap_base_out_amount;
    }

    public UnsignedLong getSwap_quote2_base_fee() {
        return swap_quote2_base_fee;
    }

    public PubKey getBase_vault() {
        return base_vault;
    }

    public PubKey getQuote_vault() {
        return quote_vault;
    }

    public PubKey getBase_mint() {
        return base_mint;
    }

    public PubKey getQuote_mint() {
        return quote_mint;
    }

    public PubKey getLp_mint() {
        return lp_mint;
    }

    public PubKey getOpen_orders() {
        return open_orders;
    }

    public PubKey getMarket_id() {
        return market_id;
    }

    public PubKey getMarket_program_id() {
        return market_program_id;
    }

    public PubKey getTarget_orders() {
        return target_orders;
    }

    public PubKey getWithdraw_queue() {
        return withdraw_queue;
    }

    public PubKey getLp_vault() {
        return lp_vault;
    }

    public PubKey getOwner() {
        return owner;
    }

    public PubKey getPnl_owner() {
        return pnl_owner;
    }
}
