package acalidonio.bodegamovilbackend.domain.dto.request;

import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @NotBlank(message = "El nombre es requerido")
    private String name;

    private String description;
    
    @NotNull(message = "La categoría es requerida")
    private ProductCategory category;

    private String location;

    @NotNull(message = "El stock es requerido")
    @PositiveOrZero(message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    private String innerDiameter;
    private String outerDiameter;
    private String width;
    private String weight;
    private String material;
    private String imageUrl;
}
