package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.DietSerializerDTO;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Diet;

import java.io.IOException;

public class DietSerializer extends StdSerializer<Diet> {
    public DietSerializer(){
        this(null);
    }
    public DietSerializer(Class<Diet> t) {
        super(t);
    }

    @Override
    public void serialize(Diet diet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DietSerializerDTO result = new DietSerializerDTO(diet.getId(), diet.getRecommendations(), diet.getSchedule(), diet.getPreferences());
        jsonGenerator.writeObject(result);
    }
}
