package com.hospital.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", url = "http://localhost:8084")
public interface DoctorClient {

    @GetMapping("/api/doctores/{id}")
    Object obtenerDoctorPorId(@PathVariable("id") Long id);
}