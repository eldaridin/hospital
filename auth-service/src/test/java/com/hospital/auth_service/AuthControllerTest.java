package com.hospital.auth_service;

import com.hospital.auth_service.controller.AuthController;
import com.hospital.auth_service.dto.LoginResponse;
import com.hospital.auth_service.exception.AuthException;
import com.hospital.auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private String loginRequestJson;
    private String loginRequestInvalidoJson;
    private LoginResponse loginResponseReal;

    @BeforeEach
    void setUp() {
        loginRequestJson = """
            {
                "username": "admin",
                "password": "secreta123"
            }
        """;

        loginRequestInvalidoJson = """
            {
                "password": "secreta123"
            }
        """;

        loginResponseReal = new LoginResponse("Usuario autorizado", "fake-jwt-token", "admin", "ADMIN");
    }

    @Test
    @WithMockUser
    void publico_DeberiaRetornar200YMensaje() throws Exception {
        mockMvc.perform(get("/auth/publico"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auth Service activo"));
    }

    @Test
    @WithMockUser
    void login_DeberiaRetornar200YResponse_CuandoCredencialesSonValidas() throws Exception {
        when(authService.login(eq("admin"), eq("secreta123"))).thenReturn(loginResponseReal);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario autorizado"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void login_DeberiaRetornar400_CuandoFaltanDatos() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestInvalidoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_DeberiaLanzarAuthException_CuandoServicioFalla() throws Exception {
        when(authService.login(anyString(), anyString()))
                .thenThrow(new AuthException("Password incorrecta"));
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthException));
    }
}