package dev.whips.solana4j.data.providers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.primitives.UnsignedLong;

import java.io.IOException;

public class JacksonMappingsProvider {
    public static ObjectMapper createObjectMapper(){
        SimpleModule module = new SimpleModule("Solana4j");

        module.addDeserializer(UnsignedLong.class, new UnsignedLongDeserializer());
        module.addSerializer(UnsignedLong.class, new UnsignedLongSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static class UnsignedLongSerializer extends JsonSerializer<UnsignedLong> {
        @Override
        public void serialize(UnsignedLong value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toString(10));
        }
    }

    public static class UnsignedLongDeserializer extends JsonDeserializer<UnsignedLong> {
        @Override
        public UnsignedLong deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return UnsignedLong.valueOf(p.getValueAsString(), 10);
        }
    }
}
