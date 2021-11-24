package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.IdClass;
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
        List<IdClass> ids = new ArrayList<>();
        for (Animal animal : animals){
            IdClass idObj = new IdClass(animal.getId());
            ids.add(idObj);
        }
        jsonGenerator.writeObject(ids);
    }
}
