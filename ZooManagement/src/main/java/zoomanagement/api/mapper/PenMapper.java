package zoomanagement.api.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Pen;

public class PenMapper{
    public static PenDTO mapToDto(Pen pen) {
        if (pen == null) {
            return null;
        } else {
            return PenDTO.builder()
                    .name(pen.getName())
                    .location(pen.getLocation())
                    .surface(pen.getSurface())
                    .description(pen.getDescription())
                    .speciesName(pen.getSpecies().getName())
                    .build();
        }
    }
}
