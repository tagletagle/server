package com.example.tagletagle.src.board.controller;

import com.example.tagletagle.src.board.dto.*;
import com.example.tagletagle.src.board.dto.SearchResponseDTO;
import org.springframework.web.bind.annotation.*;

import com.example.tagletagle.src.user.dto.FollowsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.board.service.BoardService;
import com.example.tagletagle.src.user.dto.UserBasicInfoDTO;
import com.example.tagletagle.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import javax.naming.directory.SearchResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@Operation(summary = "게시글 작성 api", description = "프론트에서 createPostDTO에 해당하는 값을 넘겨받아 게시글을 작성합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
	})
	@PostMapping("/api/board/post")
	public ResponseEntity<BaseResponse<String>> createPost(@Valid @RequestBody CreatePostDTO createPostDTO){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			boardService.createPost(userId, createPostDTO);

			return ResponseEntity.ok(new BaseResponse<>("게시글이 생성되었습니다."));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@Operation(summary = "게시글 리스트 조회(특정 userId에 대한) api", description = "url에 author_id를 적어 해당 id의 user가 작성한 게시글 리스트를 조회합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "500", description = "저자가 존재하지 않습니다"),
	})
	@GetMapping("/api/board/post/user/{author_id}")
	public ResponseEntity<BaseResponse<PostsDTO>> getPostsByUser(@PathVariable("author_id")Long authorId){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			PostsDTO postsDTO = boardService.getPostsByUser(userId, authorId);

			return ResponseEntity.ok(new BaseResponse<>(postsDTO));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@GetMapping("/api/board/post/user/{author_id}/tag/{tag_id}")
	@Operation(summary = "게시글 리스트 조회(특정 userId, 특정 태그) api", description = "url에 author_id와 tag_name을 적어 해당 author_id의 user가 작성한 게시글 중 특정 태그가 포함된 게시글 리스트를 조회합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "500", description = "저자가 존재하지 않습니다"),
	})
	public ResponseEntity<BaseResponse<PostsDTO>> getPostsByUserWithTag(@PathVariable("author_id")Long authorId, @PathVariable("tag_id")Long tagId){
		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			PostsDTO postsDTO = boardService.getPostsByUserWithTag(userId, authorId, tagId);

			return ResponseEntity.ok(new BaseResponse<>(postsDTO));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}


	@PatchMapping("api/board/post/like/{post_id}")
	@Operation(summary = "좋아요 설정/좋아요 해제 api", description = "post_id의 해당하는 게시글에 로그인 유저가 기존에 좋아요가 안 되어있으면 설정, 되어있다면 좋아요를 해제합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "500", description = "게시글이 존재하지 않습니다"),

	})
	public ResponseEntity<BaseResponse<String>> likeOrUnLikePost(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = boardService.likeOrUnLikePost(userId, postId);

			return ResponseEntity.ok(new BaseResponse<>(comment));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@PatchMapping("api/board/post/scrap/{post_id}")
	@Operation(summary = "스크랩 설정/좋아요 해제 api", description = "post_id의 해당하는 게시글에 로그인 유저가 기존에 스크랩이 안 되어있으면 설정, 되어있다면 좋아요를 해제합니다", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "500", description = "게시글이 존재하지 않습니다"),

	})
	public ResponseEntity<BaseResponse<String>> scrapOrUnScrapPost(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			String comment = boardService.scrapOrUnScrapPost(userId, postId);

			return ResponseEntity.ok(new BaseResponse<>(comment));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}

	}

	@GetMapping("api/board/comment/{post_id}")
	@Operation(summary = "게시글 댓글 리스트 조회 api", description = "post_id의 해당하는 게시글에 댓글 리스트를 조회", responses = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "파라미터 오류"),
		@ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다"),
		@ApiResponse(responseCode = "500", description = "게시글이 존재하지 않습니다"),

	})
	public ResponseEntity<BaseResponse<CommentsDTO>> getCommentListByPostId(@PathVariable("post_id")Long postId){

		try{
			Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

			CommentsDTO commentsDTO = boardService.getCommentListByPostId(userId, postId);

			return ResponseEntity.ok(new BaseResponse<>(commentsDTO));

		}catch (BaseException e){
			HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
		}
	}


	@GetMapping("/api/board/hot")
	public ResponseEntity<List<BoardResponseDTO>> getHotBoard(
			@RequestParam(required = false, defaultValue = "5") Long likeCount
	){
		List<BoardResponseDTO> hotPosts = boardService.getHotBoard(likeCount);
		return ResponseEntity.ok(hotPosts);
	}
  
	@GetMapping("/api/board/search/history")
	public List<SearchHistoryDTO> getFSearchHistoryList(){
		Long userId = SecurityUtil.getCurrentUserId()
				.orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

		return boardService.getUserSearchHistory(userId);

	}

	@GetMapping("/api/board/search/result")
	public ResponseEntity<SearchResponseDTO> getSearchResultList(@RequestParam("keyword") String keyword) {

		return ResponseEntity.ok(boardService.getSearchResultList(keyword));
    }
}
