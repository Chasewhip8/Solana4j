package dev.whips.solana4j.utils.serialize;

import com.google.common.primitives.UnsignedLong;
import dev.whips.solana4j.client.data.PubKey;

public class ByteDeserializers {
    public static final PubKeyDeserializer PUBKEY =
            new PubKeyDeserializer();

    public static final COptionDeserializer<PubKey> C_OPTION_PUBKEY =
            new COptionDeserializer<>(PUBKEY);

    public static final UnsignedLongDeserializer U64 =
            new UnsignedLongDeserializer(64);

    public static final COptionDeserializer<UnsignedLong> C_OPTION_U64 =
            new COptionDeserializer<>(U64);
}
