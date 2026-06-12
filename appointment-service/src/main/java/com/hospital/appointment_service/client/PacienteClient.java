package com.hospital.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "http://localhost:8081")
public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    Object obtenerPacientePorId(@PathVariable("id") Long id);
}