package dev.whips.solana4j;

import dev.whips.solana4j.client.data.PubKey;
import dev.whips.solana4j.utils.DataReadException;
import dev.whips.solana4j.utils.DataReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataReaderTest {
    @Test
    public void readOverflow(){
        byte[] bytes = {(byte) 255, (byte) 255, (byte) 2, (byte) 255}; // U32

        DataReader dataReader = new DataReader(bytes);
        assertEquals(4, dataReader.getRemainingBytes()); // Make sure everything's in there
        assertEquals("4278386687", dataReader.readU32().toString());
        assertEquals(0, dataReader.getRemainingBytes());

        try {
            dataReader.readPubKey();
            fail();
        } catch (DataReadException e){
            // Should throw
        }
    }

    @Test
    public void readSingleU32(){
        byte[] bytes = {(byte) 255, (byte) 255, (byte) 2, (byte) 255}; // U32

        DataReader dataReader = new DataReader(bytes);
        assertEquals(4, dataReader.getRemainingBytes()); // Make sure everything's in there
        assertEquals("4278386687", dataReader.readU32().toString());
        assertEquals(0, dataReader.getRemainingBytes());
    }

    @Test
    public void readMultipleTypes(){
        PubKey wallet = new PubKey("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT");

        byte[] bytes = {(byte) 255, (byte) 255, (byte) 255, (byte) 255, // U32
                (byte) 255, (byte) 255, (byte) 255, (byte) 255, // U32
                (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, // U64
                (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
                (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, // U128
        };

        // add pubkey byte[] to end of previous array
        byte[] newArr = new byte[bytes.length + 32];
        System.arraycopy(bytes, 0, newArr, 0, bytes.length);
        byte[] key = wallet.getRawKey();
        System.arraycopy(key, 0, newArr, bytes.length, key.length);

        DataReader dataReader = new DataReader(newArr);

        assertEquals(64, dataReader.getRemainingBytes()); // Make sure everything's in there

        // U32
        assertEquals("4294967295", dataReader.readU32().toString());
        assertEquals(64 - 4, dataReader.getRemainingBytes());
        assertEquals("4294967295", dataReader.readU32().toString());
        assertEquals(64 - 4 - 4, dataReader.getRemainingBytes());
        // U64
        assertEquals("18446744073709551615", dataReader.readU64().toString());
        assertEquals(64 - 4 - 4 - 8, dataReader.getRemainingBytes());
        // U128
        assertEquals("340282366920938463463374607431768211455", dataReader.readU128().toString());
        assertEquals(64 - 4 - 4 - 8 - 16, dataReader.getRemainingBytes());
        assertEquals("24y6Hi2nUCjAP7Lzxm1kqMjA2UfUMMosKkETxJeqMcWT", dataReader.readPubKey().toString());
        assertEquals(64 - 4 - 4 - 8 - 16 - 32, dataReader.getRemainingBytes());
    }
}
