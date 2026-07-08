package acalidonio.bodegamovilbackend.service;

import acalidonio.bodegamovilbackend.domain.dto.response.DashboardStatsResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import java.util.List;

public interface DashboardService {
    DashboardStatsResponse getStats();
    List<ProductResponse> getRecentProducts();
}
