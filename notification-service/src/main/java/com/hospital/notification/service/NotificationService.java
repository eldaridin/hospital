package com.hospital.notification.service;

import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationLog sendNotification(NotificationLog log) {
        log.setSentAt(LocalDateTime.now());
        System.out.println("Enviando correo de tipo " + log.getNotificationType() + " a: " + log.getRecipientEmail());
        return repository.save(log);
    }

    public List<NotificationLog> getLogsByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }
}