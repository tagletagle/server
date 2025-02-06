package com.example.tagletagle.src.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.notification.dto.NotificationInfoDTO;
import com.example.tagletagle.src.notification.dto.NotificationsDTO;
import com.example.tagletagle.src.notification.entity.NotificationEntity;
import com.example.tagletagle.src.notification.repository.NotificationRepository;
import com.example.tagletagle.src.notification.repository.NotificationTempRepository;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final UserRepository userRepository;
	private final NotificationTempRepository notificationTempRepository;

	public NotificationsDTO findNotificationList(Long userId) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		List<NotificationInfoDTO> notificationInfoDTOList = notificationTempRepository.findNotificationInfoList(userId);

		NotificationsDTO notificationsDTO = new NotificationsDTO();
		notificationsDTO.setNotificationInfoDTOList(notificationInfoDTOList);

		return notificationsDTO;
	}
}
