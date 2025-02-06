package com.example.tagletagle.src.user.controller;


import com.example.tagletagle.src.user.dto.UserProfileResponseDTO;
import com.example.tagletagle.src.user.entity.UserEntity;

import com.example.tagletagle.src.board.dto.PostsDTO;
import com.example.tagletagle.src.user.dto.FollowsDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.src.user.service.UserService;
import com.example.tagletagle.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PatchMapping("/api/user/basic/info")
	@Operation(summary = "유저에 대한 기본정보를 작성/수정 api", description = "UserBasicInfoDTO를 받아 user의 기본 정보를 작성 및 수정 하는 api입니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "400", description = "유저가 존재하지 않습니다"),
	})
	public ResponseEntity<BaseResponse<String>> saveOrUpdateUserBasicInfo(@Valid @RequestBody UserBasicInfoDTO userBasicInfoDTO){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			userService.saveOrUpdateUserBasicInfo(userId, userBasicInfoDTO);

			return ResponseEntity.ok(new BaseResponse<>("기본정보가 입력되었습니다."));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@PatchMapping("/api/user/following/{following_user_id}")
	@Operation(summary = "팔로잉 설정/해제 api", description = "url로 following_user_id를 받아 로그인한 유저가 해당 following 유저를 팔로잉 설정/해제 하는 api입니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "400", description = "유저가 존재하지 않습니다"),
	})
	public ResponseEntity<BaseResponse<String>> followUser(@PathVariable("following_user_id")Long followingUserId){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = userService.followUser(userId, followingUserId);

			System.out.println("userId : " + userId); //팔로잉 하는 사람(팔로워)
			System.out.println("followingUserId : " + followingUserId); //팔로잉 당하는 사람

			return ResponseEntity.ok(new BaseResponse<>(comment));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@GetMapping("/api/user/nickname/check/{nickname}")
	@Operation(summary = "닉네임 중복확인 api", description = "String 자료형: nickname 을 받아 해당하는 username이 존재하는지 검사합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	public ResponseEntity<BaseResponse<Boolean>> nicknameDupCheck(@PathVariable String nickname){
		try{
			return ResponseEntity.ok(new BaseResponse<>(userService.nicknameDupCheck(nickname)));


		}catch (BaseException e) {
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}


	//사용자 프로필 조회
	@GetMapping("/api/user/{userId}/profile")
	@Operation(summary = "사용자 프로필을 조회하는 api", description = "url로 user_id를 받아 해당 user의 프로필을 조회하는 api입니다", responses = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "파라미터 오류"),
			@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable Long userId){
		UserProfileResponseDTO userProfile = userService.getUserProfile(userId);
		return ResponseEntity.ok(userProfile);
	}


	@GetMapping("/api/user/following/{follower}")
	@Operation(summary = "팔로잉 목록을 조회하는 api", description = "url로 user_id를 받아 해당 user의 팔로잉 목록을 조회하는 api입니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	public ResponseEntity<BaseResponse<List<FollowsDTO>>> getFollowingList(@PathVariable Long follower){
		try {
			return ResponseEntity.ok(new BaseResponse<>(userService.getFollowingUsers(follower)));
		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}
	}

	@GetMapping("/api/user/follower/{following_user_id}")
	@Operation(summary = "팔로워 목록을 조회하는 api", description = "url로 user_id를 받아 해당 user의 팔로워 목록을 조회하는 api입니다", responses = {
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400", description = "파라미터 오류"),
			@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	public ResponseEntity<BaseResponse<List<FollowsDTO>>> getFollowerList(@PathVariable Long following_user_id) {
		try{
			return ResponseEntity.ok(new BaseResponse<>(userService.getFollowerUsers(following_user_id)));
		}catch (BaseException e) {
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}
	}

}
