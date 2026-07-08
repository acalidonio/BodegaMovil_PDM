package acalidonio.bodegamovilbackend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    private String sku;
    
    private String name;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    
    @Enumerated(EnumType.STRING)
    private StockStatus status;
    private String location;
    private int stock;
    private Date lastAudit;
    
    // Technical specs
    private String innerDiameter;
    private String outerDiameter;
    private String width;
    private String weight;
    private String material;
    
    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private boolean active = true;
    
    private String imageUrl;
}

