package com.hospital.notification.controller;

import com.hospital.notification.model.NotificationLog;
import com.hospital.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Endpoints para el envío de alertas hospitalarias")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @Operation(summary = "Enviar alerta automatizada", description = "Registra y despacha una notificación en el sistema")
    @PostMapping
    public ResponseEntity<NotificationLog> dispatchNotification(@RequestBody NotificationLog log) {
        return new ResponseEntity<>(service.sendNotification(log), HttpStatus.CREATED);
    }

    @Operation(summary = "Historial de alertas por paciente", description = "Obtiene todas las notificaciones recibidas por un usuario")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NotificationLog>> getHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getLogsByPatient(patientId));
    }
}