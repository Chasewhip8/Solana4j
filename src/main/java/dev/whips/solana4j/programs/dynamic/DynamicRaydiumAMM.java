package dev.whips.solana4j.programs.dynamic;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.SolanaAPI;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.programs.ContractUpdateListener;
import dev.whips.solana4j.programs.DynamicProgram;
import dev.whips.solana4j.programs.UpdatePropagator;
import dev.whips.solana4j.utils.DataReader;
import dev.whips.solana4j.utils.DataUtils;

import java.math.BigInteger;

public class DynamicRaydiumAMM extends DynamicProgram {
    private UnsignedLong status;
    private UnsignedLong nonce;
    private UnsignedLong max_order;
    private UnsignedLong depth;
    /// minimal decimal step amid orders in relation to decimals of relevant mint
    private UnsignedLong base_decimal;
    private UnsignedLong quote_decimal;
    private UnsignedLong state;
    private UnsignedLong reset_flag;
    /// min size of trade in quote
    private UnsignedLong min_size;
    private UnsignedLong vol_max_cut_ratio;
    private UnsignedLong amount_wave_ratio;
    private UnsignedLong base_lot_size;
    private UnsignedLong quote_lot_size;
    private UnsignedLong min_price_multiplier;
    private UnsignedLong max_price_multiplier;
    private UnsignedLong system_decimal_value;
    private UnsignedLong min_separate_numerator;
    private UnsignedLong min_separate_denominator;
    private UnsignedLong trade_fee_numerator;
    private UnsignedLong trade_fee_denominator;
    private UnsignedLong pnl_numerator;
    private UnsignedLong pnl_denominator;
    private UnsignedLong swap_fee_numerator;
    private UnsignedLong swap_fee_denominator;
    private UnsignedLong base_need_take_pnl;
    private UnsignedLong quote_need_take_pnl;
    /// accrued not yet withdraw fee of quote
    private UnsignedLong quote_total_pnl;
    /// accrued not yet withdraw fee of base
    private UnsignedLong base_total_pnl;
    private BigInteger quote_total_deposited;
    private BigInteger base_total_deposited;
    private BigInteger swap_base_in_amount;
    private BigInteger swap_quote_out_amount;
    // total fee accrued
    private UnsignedLong swap_base2_quote_fee;
    private BigInteger swap_quote_in_amount;
    private BigInteger swap_base_out_amount;
    // total fee accrued
    private UnsignedLong swap_quote2_base_fee;
    // amm vault
    /// base spl token account
    private PubKey base_vault_key;
    private DynamicSPLTokenAccount base_vault;
    /// quite spl token account
    private PubKey quote_vault_key;
    private DynamicSPLTokenAccount quote_vault;
    // mint
    private PubKey base_mint;
    private PubKey quote_mint;
    private PubKey lp_mint;
    // market
    /// orders on market done by this pool
    private PubKey open_orders_key;
    private DynamicSerumOpenOrders open_orders;
    /// usually order book, usually serum
    private PubKey market_id;
    private PubKey market_program_id;
    private PubKey target_orders;
    private PubKey withdraw_queue;
    private PubKey lp_vault;
    private PubKey owner;
    private PubKey pnl_owner;

    // Propagators
    private UpdatePropagator<Double> pricePropagator;

    public DynamicRaydiumAMM(SolanaAPI api, PubKey pubKey) {
        super(api, pubKey);
    }

    public void subscribePrice(ContractUpdateListener<Double> listener){
        if (pricePropagator == null){
            pricePropagator = new UpdatePropagator<>();

            base_vault.registerUpdateListener((slot) -> pricePropagator.propagateUpdate(slot, getPrice()));
            quote_vault.registerUpdateListener((slot) -> pricePropagator.propagateUpdate(slot, getPrice()));
            open_orders.registerUpdateListener((slot) -> pricePropagator.propagateUpdate(slot, getPrice()));
            registerUpdateListener((slot) -> {
                //System.out.println("Current State: " + state);
                pricePropagator.propagateUpdate(slot, getPrice());
            });

//            base_vault.registerUpdateListener(() -> {
//                computeDiffs();
//            });
//            quote_vault.registerUpdateListener(() -> {
//                computeDiffs();
//            });
//            open_orders.registerUpdateListener(() -> {
//                computeDiffs();
//            });
//            registerUpdateListener(() -> {
//                computeDiffs();
//            });

            api.getUpdatePropagatorManager().registerPropagator(pricePropagator);
        }

        pricePropagator.addListener(listener);
    }

    public double getPrice() {
        int baseDecimals = getBase_decimal().intValue();
        int quoteDecimals = getQuote_decimal().intValue();

        double base = DataUtils.toDecimal(base_vault.getAmount().longValue(), baseDecimals);
        double quote = DataUtils.toDecimal(quote_vault.getAmount().longValue(), quoteDecimals);

        double baseTotal = base + DataUtils.toDecimal(open_orders.getBaseTokenTotal().longValue(), baseDecimals)
                - DataUtils.toDecimal(getBase_need_take_pnl().longValue(), baseDecimals);
        double quoteTotal = quote + DataUtils.toDecimal(open_orders.getQuoteTokenTotal().longValue(), quoteDecimals)
                - DataUtils.toDecimal(getQuote_need_take_pnl().longValue(), quoteDecimals);

        return quoteTotal / baseTotal;
    }

    @Override
    public int getExpectedDataLength() {
        return 752;
    }

    @Override
    public void updateProgramData(DataReader dataReader) {
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
        this.base_vault_key = dataReader.readPubKey();
        this.quote_vault_key = dataReader.readPubKey();
        this.base_mint = dataReader.readPubKey();
        this.quote_mint = dataReader.readPubKey();
        this.lp_mint = dataReader.readPubKey();
        this.open_orders_key = dataReader.readPubKey();
        this.market_id = dataReader.readPubKey();
        this.market_program_id = dataReader.readPubKey();
        this.target_orders = dataReader.readPubKey();
        this.withdraw_queue = dataReader.readPubKey();
        this.lp_vault = dataReader.readPubKey();
        this.owner = dataReader.readPubKey();
        this.pnl_owner = dataReader.readPubKey();

        try {
            this.base_vault = needsUpdating(base_vault, base_vault_key)
                    ? updateAccount(this.base_vault, new DynamicSPLTokenAccount(api, base_vault_key)) : this.base_vault;
            this.quote_vault = needsUpdating(quote_vault, quote_vault_key)
                    ? updateAccount(this.quote_vault, new DynamicSPLTokenAccount(api, quote_vault_key)) : this.quote_vault;
            this.open_orders = needsUpdating(open_orders, open_orders_key)
                    ? updateAccount(this.open_orders, new DynamicSerumOpenOrders(api, open_orders_key)) : this.open_orders;
        } catch (Exception e){
            e.printStackTrace();
        }
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

    public DynamicSPLTokenAccount getBase_vault() {
        return base_vault;
    }

    public DynamicSPLTokenAccount getQuote_vault() {
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

    public DynamicSerumOpenOrders getOpen_orders() {
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

    public PubKey getBase_vault_key() {
        return base_vault_key;
    }

    public PubKey getQuote_vault_key() {
        return quote_vault_key;
    }

    public PubKey getOpen_orders_key() {
        return open_orders_key;
    }

    @Override
    public String toString() {
        return "DynamicRaydiumAMM{" +
                "\nstatus=" + status +
                "\n, nonce=" + nonce +
                "\n, max_order=" + max_order +
                "\n, depth=" + depth +
                "\n, base_decimal=" + base_decimal +
                "\n, quote_decimal=" + quote_decimal +
                "\n, state=" + state +
                "\n, reset_flag=" + reset_flag +
                "\n, min_size=" + min_size +
                "\n, vol_max_cut_ratio=" + vol_max_cut_ratio +
                "\n, amount_wave_ratio=" + amount_wave_ratio +
                "\n, base_lot_size=" + base_lot_size +
                "\n, quote_lot_size=" + quote_lot_size +
                "\n, min_price_multiplier=" + min_price_multiplier +
                "\n, max_price_multiplier=" + max_price_multiplier +
                "\n, system_decimal_value=" + system_decimal_value +
                "\n, min_separate_numerator=" + min_separate_numerator +
                "\n, min_separate_denominator=" + min_separate_denominator +
                "\n, trade_fee_numerator=" + trade_fee_numerator +
                "\n, trade_fee_denominator=" + trade_fee_denominator +
                "\n, pnl_numerator=" + pnl_numerator +
                "\n, pnl_denominator=" + pnl_denominator +
                "\n, swap_fee_numerator=" + swap_fee_numerator +
                "\n, swap_fee_denominator=" + swap_fee_denominator +
                "\n, base_need_take_pnl=" + base_need_take_pnl +
                "\n, quote_need_take_pnl=" + quote_need_take_pnl +
                "\n, quote_total_pnl=" + quote_total_pnl +
                "\n, base_total_pnl=" + base_total_pnl +
                "\n, quote_total_deposited=" + quote_total_deposited +
                "\n, base_total_deposited=" + base_total_deposited +
                "\n, swap_base_in_amount=" + swap_base_in_amount +
                "\n, swap_quote_out_amount=" + swap_quote_out_amount +
                "\n, swap_base2_quote_fee=" + swap_base2_quote_fee +
                "\n, swap_quote_in_amount=" + swap_quote_in_amount +
                "\n, swap_base_out_amount=" + swap_base_out_amount +
                "\n, swap_quote2_base_fee=" + swap_quote2_base_fee +
                "\n, base_vault_key=" + base_vault_key +
                "\n, base_vault=" + base_vault +
                "\n, quote_vault_key=" + quote_vault_key +
                "\n, quote_vault=" + quote_vault +
                "\n, base_mint=" + base_mint +
                "\n, quote_mint=" + quote_mint +
                "\n, lp_mint=" + lp_mint +
                "\n, open_orders_key=" + open_orders_key +
                "\n, open_orders=" + open_orders +
                "\n, market_id=" + market_id +
                "\n, market_program_id=" + market_program_id +
                "\n, target_orders=" + target_orders +
                "\n, withdraw_queue=" + withdraw_queue +
                "\n, lp_vault=" + lp_vault +
                "\n, owner=" + owner +
                "\n, pnl_owner=" + pnl_owner +
                '}';
    }
}
