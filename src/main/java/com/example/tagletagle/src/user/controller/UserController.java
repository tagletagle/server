package com.example.tagletagle.src.user.controller;

import com.example.tagletagle.src.board.dto.PostsDTO;
import com.example.tagletagle.src.user.dto.FollowsDTO;
import org.springframework.web.bind.annotation.*;


import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.src.user.service.UserService;
import com.example.tagletagle.utils.SecurityUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PatchMapping("/api/user/basic/info")
	public BaseResponse<String> saveOrUpdateUserBasicInfo(@Valid @RequestBody UserBasicInfoDTO userBasicInfoDTO){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			userService.saveOrUpdateUserBasicInfo(userId, userBasicInfoDTO);

			return new BaseResponse<>("기본정보가 입력되었습니다.");

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@PatchMapping("/api/user/following/{following_user_id}")
	public BaseResponse<String> followUser(@PathVariable("following_user_id")Long followingUserId){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = userService.followUser(userId, followingUserId);

			return new BaseResponse<>(comment);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@GetMapping("/api/user/nickname/check/{nickname}")
	public boolean nicknameDupCheck(@PathVariable String nickname){
		try{
			return userService.nicknameDupCheck(nickname);


		}catch (BaseException e) {
			return false;
		}

	}

	@GetMapping("/api/user/following/{follower}")
	public List<FollowsDTO> getFollowingList(@PathVariable Long follower){

			return userService.getFollowingUsers(follower);

	}


}
