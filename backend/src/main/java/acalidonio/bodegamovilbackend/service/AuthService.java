package acalidonio.bodegamovilbackend.service;

import acalidonio.bodegamovilbackend.domain.dto.response.AuthResponse;
import acalidonio.bodegamovilbackend.domain.dto.request.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
