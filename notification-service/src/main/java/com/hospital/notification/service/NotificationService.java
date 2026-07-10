package com.hospital.notification.service;

import com.hospital.notification.dto.NotificationDTO;
import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationLog sendFromDTO(NotificationDTO dto) {
        logger.info("Enviando notificacion tipo {} a: {}", dto.getNotificationType(), dto.getRecipientEmail());

        NotificationLog log = new NotificationLog();
        log.setPatientId(dto.getPatientId());
        log.setRecipientEmail(dto.getRecipientEmail());
        log.setMessage(dto.getMessage());
        log.setNotificationType(dto.getNotificationType());
        log.setSentAt(LocalDateTime.now());

        NotificationLog guardado = repository.save(log);
        logger.info("Notificacion enviada exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    public List<NotificationLog> getLogsByPatient(Long patientId) {
        logger.info("Obteniendo notificaciones del paciente ID: {}", patientId);
        return repository.findByPatientId(patientId);
    }
}
