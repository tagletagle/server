package com.example.tagletagle.src.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.src.notification.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
