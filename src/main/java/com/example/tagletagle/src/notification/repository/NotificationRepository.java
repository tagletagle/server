package com.example.tagletagle.src.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tagletagle.src.notification.entity.NotificationEntity;
import com.example.tagletagle.src.user.entity.UserEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	List<NotificationEntity> findNotificationEntitiesByUser(UserEntity user);

}
