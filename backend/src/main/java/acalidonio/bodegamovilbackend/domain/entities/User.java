package acalidonio.bodegamovilbackend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    private String employeeId;
    
    private String name;

    private String initials;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private String passwordHash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_shift_templates",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_template_id")
    )
    private Set<ShiftTemplate> shiftTemplates = new HashSet<>();
    
    private String imageUrl;
}

