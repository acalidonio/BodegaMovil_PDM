package acalidonio.bodegamovilbackend.service.impl;

import acalidonio.bodegamovilbackend.common.mappers.ProductMapper;
import acalidonio.bodegamovilbackend.domain.dto.request.CreateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.request.UpdateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import acalidonio.bodegamovilbackend.exceptions.BusinessRuleException;
import acalidonio.bodegamovilbackend.exceptions.ResourceNotFoundException;
import acalidonio.bodegamovilbackend.domain.entities.Product;
import acalidonio.bodegamovilbackend.repository.ProductRepository;
import acalidonio.bodegamovilbackend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByActiveTrue(pageable);
        return productMapper.toDtoList(products);
    }
    
    @Override
    public Page<ProductResponse> searchProducts(String query, List<ProductCategory> categories, Pageable pageable) {
        boolean hasQuery = query != null && !query.trim().isEmpty();
        boolean hasCategory = categories != null && !categories.isEmpty();

        if (!hasQuery && !hasCategory) {
            return getAllProducts(pageable);
        }
        
        String safeQuery = hasQuery ? query.trim() : "";
        List<ProductCategory> safeCategories = hasCategory ? categories : null;

        Page<Product> products = productRepository.searchProducts(safeQuery, safeCategories, pageable);
        return productMapper.toDtoList(products);
    }
    
    @Override
    public ProductResponse getProductBySku(String sku) {
        Product p = productRepository.findById(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo"));
        
        return productMapper.toDto(p);
    }
    
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productMapper.toEntityCreate(request);
        product.setLastAudit(new Date());

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
    
    @Override
    public ProductResponse updateProduct(String sku, UpdateProductRequest request) {
        Product existingProduct = productRepository.findById(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o ya es inactivo"));

        if (!existingProduct.getName().equalsIgnoreCase(request.getName()) && productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessRuleException("El nombre '" + request.getName() + "' ya está en uso por otro producto.");
        }

        productMapper.updateEntityFromDto(request, existingProduct);
        existingProduct.setLastAudit(new Date());
        
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }
    
    @Override
    public void deleteProduct(String sku) {
        Product product = productRepository.findById(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o ya es inactivo"));
                
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void restoreProduct(String sku) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
                
        if (product.isActive()) {
            throw new BusinessRuleException("El producto ya se encuentra activo");
        }
        
        product.setActive(true);
        productRepository.save(product);
    }
}
