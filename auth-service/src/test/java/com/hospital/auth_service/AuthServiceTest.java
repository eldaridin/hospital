package com.hospital.auth_service;

import com.hospital.auth_service.dto.LoginResponse;
import com.hospital.auth_service.exception.AuthException;
import com.hospital.auth_service.model.Usuario;
import com.hospital.auth_service.repository.UsuarioRepository;
import com.hospital.auth_service.service.AuthService;
import com.hospital.auth_service.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuario mockUsuario;

    @BeforeEach
    void setUp() {
        mockUsuario = new Usuario();
        mockUsuario.setId(1);
        mockUsuario.setUsername("admin");
        mockUsuario.setPassword("secreta123");
        mockUsuario.setEmail("admin@hospital.com");
        mockUsuario.setRol("ADMIN");
    }

    @Test
    void login_DeberiaRetornarLoginResponse_CuandoCredencialesSonCorrectas() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(mockUsuario));
        when(jwtService.generarToken("admin", "ADMIN")).thenReturn("fake-jwt-token");
        LoginResponse resultado = authService.login("admin", "secreta123");
        assertNotNull(resultado);
        assertEquals("Usuario autorizado", resultado.getMensaje()); // Usando getMensaje() tal cual lo definiste
        assertEquals("fake-jwt-token", resultado.getToken());
        assertEquals("admin", resultado.getUsername());
        assertEquals("ADMIN", resultado.getRol());
        verify(usuarioRepository, times(1)).findByUsername("admin");
        verify(jwtService, times(1)).generarToken("admin", "ADMIN");
    }

    @Test
    void login_DeberiaLanzarAuthException_CuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());
        AuthException exception = assertThrows(AuthException.class, () -> authService.login("desconocido", "secreta123"));
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByUsername("desconocido");
        verify(jwtService, never()).generarToken(anyString(), anyString());
    }

    @Test
    void login_DeberiaLanzarAuthException_CuandoPasswordEsIncorrecta() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(mockUsuario));
        AuthException exception = assertThrows(AuthException.class, () -> authService.login("admin", "claveEquivocada"));
        assertEquals("Password incorrecta", exception.getMessage());
        verify(usuarioRepository, times(1)).findByUsername("admin");
        verify(jwtService, never()).generarToken(anyString(), anyString());
    }
}