package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Species;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesListSerializer extends StdSerializer<List<Species>> {
    public SpeciesListSerializer(){
        this(null);
    }
    public SpeciesListSerializer(Class<List<Species>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Species> speciesList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<GeneralSerializerDTO> results = new ArrayList<>();
        for (Species species : speciesList){
            GeneralSerializerDTO result = new GeneralSerializerDTO(species.getId(),species.getName());
            results.add(result);
        }
        jsonGenerator.writeObject(results);
    }
}
