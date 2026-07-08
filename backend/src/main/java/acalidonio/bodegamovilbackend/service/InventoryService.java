package acalidonio.bodegamovilbackend.service;

import acalidonio.bodegamovilbackend.domain.dto.request.CreateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.request.UpdateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(String query, List<ProductCategory> categories, Pageable pageable);
    ProductResponse getProductBySku(String sku);

    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(String sku, UpdateProductRequest request);
    void deleteProduct(String sku);
    void restoreProduct(String sku);
}
