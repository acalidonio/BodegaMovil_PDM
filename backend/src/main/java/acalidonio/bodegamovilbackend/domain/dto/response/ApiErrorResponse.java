package acalidonio.bodegamovilbackend.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private Object message;
    private LocalDateTime time;
    private String uri;
}
