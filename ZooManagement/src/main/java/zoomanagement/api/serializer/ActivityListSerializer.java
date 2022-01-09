package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityListSerializer extends StdSerializer<List<Activity>> {
    public ActivityListSerializer(){
        this(null);
    }
    public ActivityListSerializer(Class<List<Activity>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Activity> activities, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<GeneralSerializerDTO> results = new ArrayList<>();
        for (Activity activity : activities){
            GeneralSerializerDTO result = new GeneralSerializerDTO(activity.getId(),activity.getName());
            results.add(result);
        }
        jsonGenerator.writeObject(results);
    }
}
