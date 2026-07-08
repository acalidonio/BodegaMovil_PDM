package acalidonio.bodegamovilbackend.service.impl;

import acalidonio.bodegamovilbackend.domain.dto.response.AuthResponse;
import acalidonio.bodegamovilbackend.domain.dto.request.LoginRequest;
import acalidonio.bodegamovilbackend.domain.entities.User;
import acalidonio.bodegamovilbackend.exceptions.BusinessRuleException;
import acalidonio.bodegamovilbackend.repository.UserRepository;
import acalidonio.bodegamovilbackend.security.JwtUtil;
import acalidonio.bodegamovilbackend.service.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getEmployeeId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
                String token = jwtUtil.generateToken(user.getEmployeeId(), user.getRole().name());
                return new AuthResponse(token, user.getName(), user.getInitials(), user.getRole(), user.getImageUrl());
            }
        }
        throw new BusinessRuleException("Credenciales inválidas");
    }
}

