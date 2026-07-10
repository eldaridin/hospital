package com.hospital.notification.controller;

import com.hospital.notification.dto.NotificationDTO;
import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Endpoints para el envio de alertas hospitalarias")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @Operation(summary = "Enviar alerta automatizada", description = "Registra y despacha una notificacion en el sistema")
    @PostMapping
    public ResponseEntity<?> dispatchNotification(@Valid @RequestBody NotificationDTO dto) {
        logger.info("POST /api/v1/notifications - Enviando notificacion a: {}", dto.getRecipientEmail());
        try {
            NotificationLog creado = service.sendFromDTO(dto);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al enviar notificacion: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Historial de alertas por paciente", description = "Obtiene todas las notificaciones recibidas por un usuario")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NotificationLog>> getHistory(@PathVariable Long patientId) {
        logger.info("GET /api/v1/notifications/patient/{}", patientId);
        return ResponseEntity.ok(service.getLogsByPatient(patientId));
    }
}
