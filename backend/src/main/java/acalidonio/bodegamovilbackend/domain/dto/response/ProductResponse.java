package acalidonio.bodegamovilbackend.domain.dto.response;

import acalidonio.bodegamovilbackend.domain.entities.ProductCategory;
import acalidonio.bodegamovilbackend.domain.entities.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String sku;
    private String name;
    private String description;
    private ProductCategory category;
    private StockStatus status;
    private String location;
    private int stock;
    private Date lastAudit;
    private String innerDiameter;
    private String outerDiameter;
    private String width;
    private String weight;
    private String material;
    private boolean active;
    private String imageUrl;
}
