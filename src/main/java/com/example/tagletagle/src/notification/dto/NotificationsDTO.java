package com.example.tagletagle.src.notification.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "알람 목록을 담은 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsDTO {

	private List<NotificationInfoDTO> notificationInfoDTOList;

}
