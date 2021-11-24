package zoomanagement.api.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ValidationExceptionResponse {
    private LocalDateTime timestamp;
    private long status;
    private String error;
    private Map<String, String> messages;
    private String path;
}
