package com.example.tagletagle.src.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.board.dto.CommentsDTO;
import com.example.tagletagle.src.board.dto.CreatePostDTO;
import com.example.tagletagle.src.board.dto.PostsDTO;
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

	@GetMapping("/api/board/post/user/{author_id}")
	public BaseResponse<PostsDTO> getPostsByUser(@PathVariable("author_id")Long authorId){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			PostsDTO postsDTO = boardService.getPostsByUser(userId, authorId);

			return new BaseResponse<>(postsDTO);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@GetMapping("/api/board/post/user/{author_id}/tag/{tag_name}")
	public BaseResponse<PostsDTO> getPostsByUserWithTag(@PathVariable("author_id")Long authorId, @PathVariable("tag_name")String tagName){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			PostsDTO postsDTO = boardService.getPostsByUserWithTag(userId, authorId, tagName);

			return new BaseResponse<>(postsDTO);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@PatchMapping("api/board/post/like/{post_id}")
	public BaseResponse<String> likeOrUnLikePost(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = boardService.likeOrUnLikePost(userId, postId);

			return new BaseResponse<>(comment);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@PatchMapping("api/board/post/scrap/{post_id}")
	public BaseResponse<String> scrapOrUnScrapPost(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = boardService.scrapOrUnScrapPost(userId, postId);

			return new BaseResponse<>(comment);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}

	}

	@GetMapping("api/board/comment/{post_id}")
	public BaseResponse<CommentsDTO> getCommentListByPostId(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			CommentsDTO commentsDTO = boardService.getCommentListByPostId(userId, postId);

			return new BaseResponse<>(commentsDTO);

		}catch (BaseException e){
			return new BaseResponse<>(e.getStatus());
		}
	}


}
