package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Pen;

import java.io.IOException;

public class PenSerializer extends StdSerializer<Pen> {
    public PenSerializer(){
        this(null);
    }
    public PenSerializer(Class<Pen> t) {
        super(t);
    }

    @Override
    public void serialize(Pen pen, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        GeneralSerializerDTO result = new GeneralSerializerDTO(pen.getId(), pen.getName());
        jsonGenerator.writeObject(result);
    }
}
