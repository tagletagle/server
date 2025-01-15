package com.example.tagletagle.src.board.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.board.dto.CreatePostDTO;
import com.example.tagletagle.src.board.service.BoardService;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.utils.SecurityUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping("/api/board/post")
	public BaseResponse<String> createPost(@Valid @RequestBody CreatePostDTO createPostDTO){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			boardService.createPost(userId, createPostDTO);

			return new BaseResponse<>("게시글이 생성되었습니다.");

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}


}
