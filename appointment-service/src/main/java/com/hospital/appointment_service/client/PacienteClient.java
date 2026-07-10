package com.hospital.appointment_service.client;

import com.hospital.appointment_service.dto.PacienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    PacienteResponseDTO obtenerPacientePorId(@PathVariable("id") Long id);
}