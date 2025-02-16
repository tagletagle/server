package com.example.tagletagle.src.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.board.dto.CreatePostDTO;
import com.example.tagletagle.src.notification.dto.NotificationsDTO;
import com.example.tagletagle.src.notification.service.NotificationService;
import com.example.tagletagle.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/api/notification/list")
	@Operation(summary = "알람 목록 조회 api - 준현", description = "access Token에 해당하는 사용자의 알람 목록을 보여줍니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	public ResponseEntity<BaseResponse<NotificationsDTO>> findNotificationList(){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			return ResponseEntity.ok(new BaseResponse<>(notificationService.findNotificationList(userId)));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}





}
