package zoomanagement.api.DTO;

import lombok.Data;
import zoomanagement.api.domain.Diet;

import java.util.List;

@Data
public class BabyAnimal {
    public String name;
    public List<String> parents;
    public String sex;
    public String peculiarities;
    public Diet diet;
}
