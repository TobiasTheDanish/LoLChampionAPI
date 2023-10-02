package dat.sem3.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ExceptionSerializer extends StdSerializer<Exception> {
    public ExceptionSerializer() {
        super(Exception.class);
    }
    protected ExceptionSerializer(Class<Exception> t) {
        super(t);
    }
    @Override
    public void serialize(Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("errorType", e.getClass().getSimpleName());
        jsonGenerator.writeStringField("errorMessage", e.getMessage());
        jsonGenerator.writeStringField("cause", String.valueOf(e.getCause()));
        jsonGenerator.writeEndObject();
    }
}
