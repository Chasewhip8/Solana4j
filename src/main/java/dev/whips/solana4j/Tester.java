package dev.whips.solana4j;

import dev.whips.solana4j.client.data.ProgramAccount;
import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.client.data.enums.RPCEncoding;
import dev.whips.solana4j.client.data.filters.DataSizeFilter;
import dev.whips.solana4j.client.data.filters.DataSliceFilter;
import dev.whips.solana4j.client.data.filters.MemcmpFilter;
import dev.whips.solana4j.exceptions.ContractException;
import dev.whips.solana4j.exceptions.RPCException;
import dev.whips.solana4j.programs.dynamic.DynamicRaydiumAMM;
import dev.whips.solana4j.programs.layout.RaydiumAMMV4;
import dev.whips.solana4j.programs.layout.SysvarClock;
import dev.whips.solana4j.utils.DataReader;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Tester {
    private static double lastPrice;

    public static void main(String[] args) throws Exception {
        SolanaAPI solanaAPI = new SolanaAPIBuilder()
                .setCluster(SolanaCluster.GENESYS_GO_MAINNET)
                .build();

        PubKey wallet = new PubKey("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT");
        PubKey JFI_USDC_AMM_Market = new PubKey("8Skw2e6PeEvyoMGXsKAk4TLM86Qh29zRQYXzXEzxRm8Y");
        PubKey SOL_USDC_AMM_Market = new PubKey("58oQChx4yWmvKdwLLZzBi4ChoCc2fqCUWBkwMihLYQo2");

        RaydiumAMMV4 raydiumAMMV4 = new RaydiumAMMV4(solanaAPI, SOL_USDC_AMM_Market);
        System.out.println("Static: " + raydiumAMMV4.requestCurrentPrice());

        DynamicRaydiumAMM raydiumAMM = new DynamicRaydiumAMM(solanaAPI, SOL_USDC_AMM_Market);
        raydiumAMM.subscribePrice((price) -> {
            double diff = price - lastPrice;
            System.out.printf("Price Updated: " + price + " Change: %f", diff);
            System.out.println();
            lastPrice = price;
        });

/*        solanaAPI.subscribeSlot((update) -> {
            System.out.println("Slot: " + update.getParams().getResult().getSlot());
            System.out.println("Parent: " + update.getParams().getResult().getParent());
            System.out.println("Root: " + update.getParams().getResult().getRoot());
        });

        solanaAPI.subscribeAccount(new PubKey("SysvarC1ock11111111111111111111111111111111"), RPCEncoding.BASE64, (update) -> {
            try {
                System.out.println("Clock Slot: " + new SysvarClock(update.getParams().getResult().getValue().getDataReader()).getSlot());
            } catch (ContractException e){
                e.printStackTrace();
            }
        });*/

/*        PubKey saber = new PubKey("YAkoNb6HKmSxQN9L8hiBE5tPJRsniSSMzND1boHmZxe");
        SaberStableSwap saberStableSwap = new SaberStableSwap(solanaAPI, saber);

        System.out.printf("%.9f", saberStableSwap.requestRawSwapPriceRatio());
        System.out.println();*/

/*        PubKey market = new PubKey("58oQChx4yWmvKdwLLZzBi4ChoCc2fqCUWBkwMihLYQo2");
        RaydiumAMMV4 raydiumAMMV41 = new RaydiumAMMV4(solanaAPI, market);

        System.out.println("Subscribing to 1");

        Subscription<AccountInfo> subscription = solanaAPI.subscribeAccount(raydiumAMMV41.getOpen_orders(), RPCEncoding.BASE64, (data) -> {{
            try {
                SerumOpenOrdersV2 ordersV2 = new SerumOpenOrdersV2(data.getParams().getResult().getValue().getDataReader());
                System.out.println("1: " + ordersV2.getBaseTokenTotal());
            } catch (ContractException e){
                e.printStackTrace();
            }
        }});

        Thread.sleep(5000);

        System.out.println("Subscribing to 2");
        Subscription<AccountInfo> subscription2 = solanaAPI.subscribeAccount(raydiumAMMV41.getOpen_orders(), RPCEncoding.BASE64, (data) -> {{
            try {
                SerumOpenOrdersV2 ordersV2 = new SerumOpenOrdersV2(data.getParams().getResult().getValue().getDataReader());
                System.out.println("2: " + ordersV2.getBaseTokenTotal());
            } catch (ContractException e){
                e.printStackTrace();
            }
        }});

        Thread.sleep(10000);

        System.out.println("Un-Subscribing from 2");
        solanaAPI.unSubscribeAccount(subscription2);

        Thread.sleep(10000);

        System.out.println("Un-Subscribing from 1");
        solanaAPI.unSubscribeAccount(subscription);

        Thread.sleep(10000000);*/



/*        PubKey soceanJFIContract = new PubKey("ErdVK113JZZnBHnqFxrmsZ3c3RxVwCQ6xwgd1LMuLuFe");
        PubKey jungleJFIContract = new PubKey("GePFQaZKHcWE5vpxHfviQtH5jgxokSs51Y5Q4zgBiMDs");
        PubKey jungleJFILPContract = new PubKey("J2hGHwbkpj2SVo6Bs4X2Houy7n6oauydhbh9D6HpKBU4");
        PubKey jungleJFICALLContract = new PubKey("etCPUaMghWobVwxug5Vgxb6r3A7DEBdLKUywrnDrJeZ");

        System.out.println(getUniqueHolders(solanaAPI,
                soceanJFIContract, jungleJFIContract, jungleJFILPContract, jungleJFICALLContract
        ));*/
/*
        PubKey mSOL_SOL_poolAddress = new PubKey("Lee1XZJfJ9Hm2K1qTyeCz1LXNc1YBZaKZszvNY4KCDw");
        SaberStableSwap saberStableSwap = new SaberStableSwap(solanaAPI, mSOL_SOL_poolAddress);

*//*        AccountInfo accountInfo = solanaAPI.getAccountInfo(, RPCEncoding.BASE64);
        SPLTokenAccount tokenAccount = new SPLTokenAccount(accountInfo.getDataReader());*//*

        TokenBalance tokenSupply = solanaAPI.getTokenSupply(saberStableSwap.getTokenPool()).getValue();
        System.out.println(
                UnsignedLong.valueOf(tokenSupply.getAmount())
                .times(UnsignedLong.)

        );
        System.out.println(saberStableSwap.getTokenPool());*/

    }

    private static int getUniqueHolders(SolanaAPI solanaAPI, PubKey... pubKeys) throws RPCException, ContractException {
        Set<PubKey> holders = new HashSet<>();
        for (PubKey key : pubKeys) {
            final PubKey tokenAccount = new PubKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");
            List<ProgramAccount> accounts = solanaAPI.getProgramAccounts(tokenAccount, RPCEncoding.BASE64,
                    new DataSliceFilter(32, 32),
                    new DataSizeFilter(165),
                    new MemcmpFilter(0, key.getRawKey())
            );

            for (ProgramAccount programAccount : accounts){
                DataReader dataReader = programAccount.getAccount().getDataReader();
                PubKey owner = dataReader.readPubKey();
                holders.add(owner);
            }
        }
        return holders.size();
    }
}
