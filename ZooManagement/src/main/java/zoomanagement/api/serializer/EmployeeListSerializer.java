package zoomanagement.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import zoomanagement.api.DTO.GeneralSerializerDTO;
import zoomanagement.api.domain.Employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListSerializer extends StdSerializer<List<Employee>> {
    public EmployeeListSerializer(){
        this(null);
    }
    public EmployeeListSerializer(Class<List<Employee>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Employee> employees, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<GeneralSerializerDTO> results = new ArrayList<>();
        for (Employee employee : employees){
            GeneralSerializerDTO result = new GeneralSerializerDTO(employee.getId(),employee.getName());
            results.add(result);
        }
        jsonGenerator.writeObject(results);
    }
}
