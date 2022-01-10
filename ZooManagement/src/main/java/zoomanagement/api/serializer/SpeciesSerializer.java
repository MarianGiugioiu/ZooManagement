package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Species;

import java.io.IOException;

public class SpeciesSerializer extends StdSerializer<Species> {
    public SpeciesSerializer(){
        this(null);
    }
    public SpeciesSerializer(Class<Species> t) {
        super(t);
    }

    @Override
    public void serialize(Species species, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        GeneralSerializerDTO result = new GeneralSerializerDTO(species.getId(), species.getName());
        jsonGenerator.writeObject(result);
    }
}
