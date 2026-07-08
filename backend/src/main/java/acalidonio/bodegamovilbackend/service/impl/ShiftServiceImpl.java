package acalidonio.bodegamovilbackend.service.impl;

import acalidonio.bodegamovilbackend.domain.dto.response.AssignedShiftResponse;
import acalidonio.bodegamovilbackend.domain.entities.User;
import acalidonio.bodegamovilbackend.domain.entities.ShiftTemplate;
import acalidonio.bodegamovilbackend.exceptions.BusinessRuleException;
import acalidonio.bodegamovilbackend.repository.UserRepository;
import acalidonio.bodegamovilbackend.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final UserRepository userRepository;

    @Override
    public List<AssignedShiftResponse> getMyWeeklyShifts(String employeeId, int weekOffset) {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado"));
                
        Set<ShiftTemplate> templates = user.getShiftTemplates();
        List<AssignedShiftResponse> computedShifts = new ArrayList<>();
        
        // Encontrar el lunes
        LocalDate today = LocalDate.now().plusWeeks(weekOffset);
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        for (ShiftTemplate template : templates) {
            LocalDate shiftDate = startOfWeek.plusDays(template.getDayOfWeek().getValue() - 1);

            long minutes = Duration.between(template.getStartTime(), template.getEndTime()).toMinutes();
            double expectedHours = minutes / 60.0;
            
            computedShifts.add(AssignedShiftResponse.builder()
                    .name(template.getName())
                    .date(shiftDate)
                    .startTime(template.getStartTime())
                    .endTime(template.getEndTime())
                    .hoursLogged(expectedHours)
                    .build());
        }

        computedShifts.sort(Comparator.comparing(AssignedShiftResponse::getDate));
        
        return computedShifts;
    }
}
