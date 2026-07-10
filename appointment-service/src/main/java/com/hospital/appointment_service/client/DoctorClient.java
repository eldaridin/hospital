package com.hospital.appointment_service.client;

import com.hospital.appointment_service.dto.DoctorResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/api/doctores/{id}")
    DoctorResponseDTO obtenerDoctorPorId(@PathVariable("id") Long id);
}