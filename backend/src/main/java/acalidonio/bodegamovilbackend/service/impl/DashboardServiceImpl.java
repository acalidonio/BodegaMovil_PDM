package acalidonio.bodegamovilbackend.service.impl;

import acalidonio.bodegamovilbackend.common.mappers.ProductMapper;
import acalidonio.bodegamovilbackend.domain.dto.response.DashboardStatsResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.domain.entities.Product;
import acalidonio.bodegamovilbackend.repository.ProductRepository;
import acalidonio.bodegamovilbackend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public DashboardStatsResponse getStats() {
        DashboardStatsResponse stats = productRepository.getDashboardStats();
        if (stats == null) {
            return new DashboardStatsResponse(0, 0, 0);
        }

        long totalProducts = stats.getTotalProducts();
        long lowStockProducts = stats.getLowStockProducts();
        long totalStockItems = stats.getTotalStockItems();
        return new DashboardStatsResponse(totalProducts, lowStockProducts, totalStockItems);
    }

    @Override
    public List<ProductResponse> getRecentProducts() {
        List<Product> recentProducts = productRepository.findTop5ByActiveTrueOrderByLastAuditDesc();
        return recentProducts.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
