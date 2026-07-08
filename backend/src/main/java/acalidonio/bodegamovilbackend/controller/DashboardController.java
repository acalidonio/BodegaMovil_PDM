package acalidonio.bodegamovilbackend.controller;

import acalidonio.bodegamovilbackend.domain.dto.response.DashboardStatsResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.GeneralResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<GeneralResponse<DashboardStatsResponse>> getStats() {
        DashboardStatsResponse stats = dashboardService.getStats();
        return ResponseEntity.ok(
                GeneralResponse.<DashboardStatsResponse>builder()
                        .message("Estadísticas del dashboard obtenidas exitosamente")
                        .data(stats)
                        .build()
        );
    }

    @GetMapping("/recent")
    public ResponseEntity<GeneralResponse<List<ProductResponse>>> getRecentProducts() {
        List<ProductResponse> recentProducts = dashboardService.getRecentProducts();
        return ResponseEntity.ok(
                GeneralResponse.<List<ProductResponse>>builder()
                        .message("Productos recientes obtenidos exitosamente")
                        .data(recentProducts)
                        .build()
        );
    }
}
