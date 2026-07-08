package acalidonio.bodegamovilbackend.controller;

import acalidonio.bodegamovilbackend.domain.dto.response.GeneralResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.AssignedShiftResponse;
import acalidonio.bodegamovilbackend.service.ShiftService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse<List<AssignedShiftResponse>>> getMyWeeklyShifts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int weekOffset) {
            
        String employeeId = (String) request.getAttribute("username");
        List<AssignedShiftResponse> shifts = shiftService.getMyWeeklyShifts(employeeId, weekOffset);
        
        return ResponseEntity.ok(
                GeneralResponse.<List<AssignedShiftResponse>>builder()
                        .message("Turnos obtenidos exitosamente")
                        .data(shifts)
                        .build()
        );
    }
}
