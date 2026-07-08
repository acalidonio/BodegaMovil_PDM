package acalidonio.bodegamovilbackend.service;

import acalidonio.bodegamovilbackend.domain.dto.response.AssignedShiftResponse;

import java.util.List;

public interface ShiftService {
    List<AssignedShiftResponse> getMyWeeklyShifts(String employeeId, int weekOffset);
}
