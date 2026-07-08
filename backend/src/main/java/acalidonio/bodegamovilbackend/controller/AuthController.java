package acalidonio.bodegamovilbackend.controller;

import acalidonio.bodegamovilbackend.domain.dto.response.AuthResponse;
import acalidonio.bodegamovilbackend.domain.dto.response.GeneralResponse;
import acalidonio.bodegamovilbackend.domain.dto.request.LoginRequest;
import acalidonio.bodegamovilbackend.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                GeneralResponse.<AuthResponse>builder()
                        .message("Inicio de sesión exitoso")
                        .data(response)
                        .build()
        );
    }
}
