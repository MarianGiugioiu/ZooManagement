package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.IdClass;
import zoomanagement.api.domain.Animal;

import java.io.IOException;

public class AnimalSerializer extends StdSerializer<Animal> {
    public AnimalSerializer(){
        this(null);
    }
    public AnimalSerializer(Class<Animal> t) {
        super(t);
    }

    @Override
    public void serialize(Animal animal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        IdClass id = new IdClass(animal.getId());
        jsonGenerator.writeObject(id);
    }
}
