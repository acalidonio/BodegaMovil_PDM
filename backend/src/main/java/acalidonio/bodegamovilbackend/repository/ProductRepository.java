package acalidonio.bodegamovilbackend.repository;

import acalidonio.bodegamovilbackend.domain.dto.response.DashboardStatsResponse;
import acalidonio.bodegamovilbackend.domain.entities.Product;

import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByActiveTrue(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE p.active = true " +
            "AND (LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:categories IS NULL OR p.category IN :categories)"
    )
    Page<Product> searchProducts(@Param("query") String query, @Param("categories") List<ProductCategory> categories, Pageable pageable);

    boolean existsByNameIgnoreCase(String value);

    boolean existsBySkuIgnoreCase(String value);

    @Query("SELECT new acalidonio.bodegamovilbackend.domain.dto.response.DashboardStatsResponse(" +
           "COUNT(p), " +
           "SUM(CASE WHEN p.stock <= 10 THEN 1 ELSE 0 END), " +
           "SUM(p.stock)) " +
           "FROM Product p WHERE p.active = true")
    DashboardStatsResponse getDashboardStats();

    List<Product> findTop5ByActiveTrueOrderByLastAuditDesc();
}
