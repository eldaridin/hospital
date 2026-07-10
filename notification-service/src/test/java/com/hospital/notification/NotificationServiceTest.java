package com.hospital.notification;

import com.hospital.notification.dto.NotificationDTO;
import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.repository.NotificationRepository;
import com.hospital.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    private NotificationLog notificationReal;

    @BeforeEach
    void setUp() {
        notificationReal = new NotificationLog();
        notificationReal.setPatientId(100L);
        notificationReal.setRecipientEmail("paciente@test.com");
        notificationReal.setMessage("Alerta de medicamento");
        notificationReal.setNotificationType("EMAIL");
    }

    @Test
    void sendFromDTO_DeberiaEstablecerFechaYGuardar() {
        when(repository.save(any(NotificationLog.class))).thenAnswer(i -> i.getArguments()[0]);

        NotificationDTO dto = new NotificationDTO();
        dto.setPatientId(100L);
        dto.setRecipientEmail("paciente@test.com");
        dto.setMessage("Alerta de medicamento");
        dto.setNotificationType("EMAIL");

        NotificationLog resultado = service.sendFromDTO(dto);

        assertNotNull(resultado);
        assertNotNull(resultado.getSentAt());
        assertEquals("EMAIL", resultado.getNotificationType());
        verify(repository, times(1)).save(any(NotificationLog.class));
    }

    @Test
    void getLogsByPatient_DeberiaRetornarLista() {
        when(repository.findByPatientId(100L)).thenReturn(List.of(notificationReal));

        List<NotificationLog> resultado = service.getLogsByPatient(100L);

        assertFalse(resultado.isEmpty());
        assertEquals(100L, resultado.get(0).getPatientId());
        verify(repository, times(1)).findByPatientId(100L);
    }
}
