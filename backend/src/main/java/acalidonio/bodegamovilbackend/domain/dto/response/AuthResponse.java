package acalidonio.bodegamovilbackend.domain.dto.response;

import acalidonio.bodegamovilbackend.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String employeeName;
    private String initials;
    private Role role;
    private String profileImageUrl;
}

