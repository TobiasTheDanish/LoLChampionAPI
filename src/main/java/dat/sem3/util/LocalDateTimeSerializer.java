package dat.sem3.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }
    protected LocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String time = localDate.getHour() + ":" + localDate.getMinute() + ":" + localDate.getSecond();
        String date = localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth();
        String fullDate = date + " " + time;
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("full", fullDate);
        jsonGenerator.writeStringField("date", date);
        jsonGenerator.writeStringField("time", time);
        jsonGenerator.writeObjectFieldStart("raw");
        jsonGenerator.writeNumberField("year", localDate.getYear());
        jsonGenerator.writeNumberField("month", localDate.getMonthValue());
        jsonGenerator.writeNumberField("day", localDate.getDayOfMonth());
        jsonGenerator.writeNumberField("hour", localDate.getHour());
        jsonGenerator.writeNumberField("minute", localDate.getMinute());
        jsonGenerator.writeNumberField("second", localDate.getSecond());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
