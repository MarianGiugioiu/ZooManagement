package zoomanagement.api.domain.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class IdClass {
    private UUID id;

    public IdClass(UUID id) {
        this.id = id;
    }
}
