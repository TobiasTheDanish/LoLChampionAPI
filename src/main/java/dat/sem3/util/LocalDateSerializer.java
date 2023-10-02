package dat.sem3.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    public LocalDateSerializer() {
        super(LocalDate.class);
    }
    protected LocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String fullDate = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("full", fullDate);
        jsonGenerator.writeObjectFieldStart("raw");
        jsonGenerator.writeNumberField("year", localDate.getYear());
        jsonGenerator.writeNumberField("month", localDate.getMonthValue());
        jsonGenerator.writeNumberField("day", localDate.getDayOfMonth());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
