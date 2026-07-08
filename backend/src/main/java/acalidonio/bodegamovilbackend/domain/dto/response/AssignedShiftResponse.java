package acalidonio.bodegamovilbackend.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignedShiftResponse {
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double hoursLogged;
}
