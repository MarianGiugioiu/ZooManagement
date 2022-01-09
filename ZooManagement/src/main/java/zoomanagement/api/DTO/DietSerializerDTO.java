package zoomanagement.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietSerializerDTO {
    private UUID id;

    private String recommendations;

    private String schedule;

    private String preferences;
}
