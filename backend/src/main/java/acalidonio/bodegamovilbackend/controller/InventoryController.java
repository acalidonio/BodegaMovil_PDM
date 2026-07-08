package acalidonio.bodegamovilbackend.controller;

import acalidonio.bodegamovilbackend.domain.dto.request.CreateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.request.UpdateProductRequest;
import acalidonio.bodegamovilbackend.domain.dto.response.GeneralResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.PageableResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.ProductResponse;
import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import acalidonio.bodegamovilbackend.service.InventoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<GeneralResponse<PageableResponse<ProductResponse>>> getInventory(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<ProductCategory> categories,
            Pageable pageable) {
        
        Page<ProductResponse> page = inventoryService.searchProducts(query, categories, pageable);
        
        PageableResponse<ProductResponse> pageableResponse = PageableResponse.<ProductResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
                
        return ResponseEntity.ok(
                GeneralResponse.<PageableResponse<ProductResponse>>builder()
                        .message("Inventario obtenido exitosamente")
                        .data(pageableResponse)
                        .build()
        );
    }

    @GetMapping("/{sku}")
    public ResponseEntity<GeneralResponse<ProductResponse>> getProduct(@PathVariable String sku) {
        ProductResponse response = inventoryService.getProductBySku(sku);
        return ResponseEntity.ok(
                GeneralResponse.<ProductResponse>builder()
                        .message("Producto obtenido exitosamente")
                        .data(response)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<GeneralResponse<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse created = inventoryService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponse.<ProductResponse>builder()
                        .message("Producto creado exitosamente")
                        .data(created)
                        .build()
        );
    }

    @PutMapping("/{sku}")
    public ResponseEntity<GeneralResponse<ProductResponse>> updateProduct(
            @PathVariable String sku, 
            @Valid @RequestBody UpdateProductRequest request) {
            
        ProductResponse updated = inventoryService.updateProduct(sku, request);
        return ResponseEntity.ok(
                GeneralResponse.<ProductResponse>builder()
                        .message("Producto actualizado exitosamente")
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<GeneralResponse<Void>> deleteProduct(@PathVariable String sku) {
        inventoryService.deleteProduct(sku);
        return ResponseEntity.ok(
                GeneralResponse.<Void>builder()
                        .message("Producto eliminado exitosamente")
                        .build()
        );
    }

    @PatchMapping("/{sku}/restore")
    public ResponseEntity<GeneralResponse<Void>> restoreProduct(@PathVariable String sku) {
        inventoryService.restoreProduct(sku);
        return ResponseEntity.ok(
                GeneralResponse.<Void>builder()
                        .message("Producto restaurado exitosamente")
                        .build()
        );
    }
}
