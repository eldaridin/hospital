package com.hospital.notification.repository;

import com.hospital.notification.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByPatientId(Long patientId);
}