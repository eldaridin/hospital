package com.hospital.notification;

import com.hospital.notification.config.JwtTokenProvider;
import com.hospital.notification.controller.NotificationController;
import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService service;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private NotificationLog notificationReal;
    private String notificationJson;

    @BeforeEach
    void setUp() {
        notificationReal = new NotificationLog();
        notificationReal.setId(1L);
        notificationReal.setPatientId(100L);
        notificationReal.setRecipientEmail("paciente@test.com");

        notificationJson = """
            {
                "patientId": 100,
                "recipientEmail": "paciente@test.com",
                "message": "Alerta de medicamento",
                "notificationType": "EMAIL"
            }
        """;
    }

    @Test
    @WithMockUser
    void dispatchNotification_DeberiaRetornar201() throws Exception {
        when(service.sendNotification(any(NotificationLog.class))).thenReturn(notificationReal);

        mockMvc.perform(post("/api/v1/notifications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(notificationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(100));
    }

    @Test
    @WithMockUser
    void getHistory_DeberiaRetornar200YLista() throws Exception {
        when(service.getLogsByPatient(100L)).thenReturn(List.of(notificationReal));

        mockMvc.perform(get("/api/v1/notifications/patient/{patientId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(100));
    }
}