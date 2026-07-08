package acalidonio.bodegamovilbackend.common.mappers;

import acalidonio.bodegamovilbackend.domain.dto.request.CreateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.request.UpdateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.domain.entities.Product;
import acalidonio.bodegamovilbackend.domain.entities.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntityCreate(CreateProductRequest request) {
        return Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(getStockStatusFromStock(request.getStock()))
                .location(request.getLocation())
                .stock(request.getStock())
                .innerDiameter(request.getInnerDiameter())
                .outerDiameter(request.getOuterDiameter())
                .width(request.getWidth())
                .weight(request.getWeight())
                .material(request.getMaterial())
                .imageUrl(request.getImageUrl())
                .active(true)
                .build();
    }

    public void updateEntityFromDto(UpdateProductRequest request, Product entity) {
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCategory(request.getCategory());
        entity.setStatus(getStockStatusFromStock(request.getStock()));
        entity.setLocation(request.getLocation());
        entity.setStock(request.getStock());
        entity.setInnerDiameter(request.getInnerDiameter());
        entity.setOuterDiameter(request.getOuterDiameter());
        entity.setWidth(request.getWidth());
        entity.setWeight(request.getWeight());
        entity.setMaterial(request.getMaterial());
        entity.setImageUrl(request.getImageUrl());
    }

    public ProductResponse toDto(Product entity) {
        return ProductResponse.builder()
                .sku(entity.getSku())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .location(entity.getLocation())
                .stock(entity.getStock())
                .lastAudit(entity.getLastAudit())
                .innerDiameter(entity.getInnerDiameter())
                .outerDiameter(entity.getOuterDiameter())
                .width(entity.getWidth())
                .weight(entity.getWeight())
                .material(entity.getMaterial())
                .active(entity.isActive())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public Page<ProductResponse> toDtoList(Page<Product> entities) {
        return entities.map(this::toDto);
    }

    private StockStatus getStockStatusFromStock(int stock) {
        if (stock <= 0) return StockStatus.OUT_OF_STOCK;
        if (stock <= 10) return StockStatus.LOW_STOCK;
        return StockStatus.AVAILABLE;
    }
}
