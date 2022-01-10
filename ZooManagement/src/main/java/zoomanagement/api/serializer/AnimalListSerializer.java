package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Animal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimalListSerializer extends StdSerializer<List<Animal>> {
    public AnimalListSerializer(){
        this(null);
    }
    public AnimalListSerializer(Class<List<Animal>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Animal> animals, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<GeneralSerializerDTO> results = new ArrayList<>();
        for (Animal animal : animals){
            GeneralSerializerDTO result = new GeneralSerializerDTO(animal.getId(),animal.getName());
            results.add(result);
        }
        jsonGenerator.writeObject(results);
    }
}
