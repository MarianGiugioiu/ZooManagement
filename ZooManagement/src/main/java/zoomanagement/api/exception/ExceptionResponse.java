package zoomanagement.api.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {
    private LocalDateTime timestamp;
    private long status;
    private String error;
    private String message;
    private String path;
}
