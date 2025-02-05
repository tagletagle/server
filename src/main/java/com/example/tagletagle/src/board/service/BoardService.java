package com.example.tagletagle.src.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.tagletagle.src.board.dto.*;

import com.example.tagletagle.src.board.repository.*;

import com.example.tagletagle.src.board.entity.SearchHistoryEntity;

import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.board.entity.PostLikeEntity;
import com.example.tagletagle.src.board.entity.PostScrapEntity;
import com.example.tagletagle.src.tag.entity.PostTagEntity;
import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.repository.PostTagRepository;
import com.example.tagletagle.src.tag.repository.TagRepository;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final TagRepository tagRepository;
	private final PostTagRepository postTagRepository;
	private final PostLikeRepository postLikeRepository;
	private final PostScrapRepository postScrapRepository;


	private final SearchHistoryRepository searchHistoryRepository;


	@Transactional
	public void createPost(Long userId, CreatePostDTO createPostDTO) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		PostEntity post = new PostEntity(createPostDTO, user);
		postRepository.save(post);

		List<Long> tagList = createPostDTO.getTagIdList();

		if(createPostDTO.getUrl() == null){
			throw new BaseException(BaseResponseStatus.MUST_SELECT_TAG);
		}

		if(tagList.size() == 0){
			throw new BaseException(BaseResponseStatus.MUST_SELECT_TAG);
		}

		List<TagEntity> tagEntityList = tagRepository.findAllById(tagList);
		List<PostTagEntity> postTagEntityList = new ArrayList<>();
		for(int i = 0; i< tagEntityList.size(); i++){
			PostTagEntity postTag = new PostTagEntity(post, tagEntityList.get(i));
			postTagEntityList.add(postTag);
		}

		postTagRepository.saveAll(postTagEntityList);

	}


	public PostsDTO getPostsByUser(Long userId, Long authorId) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		UserEntity author = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.AUTHOR_NO_EXIST));

		PostsDTO postsDTO = new PostsDTO();
		List<PostInfoDTO> postInfoDTOList = boardRepository.findPostsByAuthorAndUser(authorId, userId);
		if(postInfoDTOList.size() == 0){
			return postsDTO;
		}


		postsDTO.setPostInfoDTOList(postInfoDTOList);

		return postsDTO;

	}

	public PostsDTO getPostsByUserWithTag(Long userId, Long authorId, Long tagId) {
		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		UserEntity author = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.AUTHOR_NO_EXIST));

		PostsDTO postsDTO = new PostsDTO();
		List<PostInfoDTO> postInfoDTOList = boardRepository.findPostsByAuthorAndUserWithTag(authorId, userId, tagId);
		if(postInfoDTOList.size() == 0){
			System.out.println("해당 태그로 작성한 글이 없습니다");
			return postsDTO;
		}


		postsDTO.setPostInfoDTOList(postInfoDTOList);

		return postsDTO;

	}

	@Transactional
	public String likeOrUnLikePost(Long userId, Long postId) {
		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		PostEntity post = postRepository.findPostEntityById(postId)
			.orElseThrow(()->new BaseException(BaseResponseStatus.POST_NO_EXIST));

		Boolean isUser = postLikeRepository.existsByPostAndUser(post, user);
		String comment = null;

		if(isUser == Boolean.TRUE){
			//좋아요가 이미 되어 있으니 해제 로직 작성
			postLikeRepository.deleteUserAllergy(user, post);
			comment = "좋아요가 해제 되었습니다";

		}else if(isUser == Boolean.FALSE){
			//좋아요가 없으니 좋아요 로직 작성
			PostLikeEntity postLike = new PostLikeEntity(post, user);
			postLikeRepository.save(postLike);
			comment = "좋아요가 설정 되었습니다";
		}

		return comment;
	}

	@Transactional
	public String scrapOrUnScrapPost(Long userId, Long postId) {
		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		PostEntity post = postRepository.findPostEntityById(postId)
			.orElseThrow(()->new BaseException(BaseResponseStatus.POST_NO_EXIST));

		Boolean isUser = postScrapRepository.existsByPostAndUser(post, user);
		String comment = null;

		if(isUser == Boolean.TRUE){
			//스크랩이 이미 되어 있으니 해제 로직 작성
			postScrapRepository.deletePostScrapEntityByUserAndPost(user, post);
			comment = "스크랩이 해제 되었습니다";

		}else if(isUser == Boolean.FALSE){
			//스크랩이 없으니 좋아요 로직 작성
			PostScrapEntity postScrap = new PostScrapEntity(post, user);
			postScrapRepository.save(postScrap);
			comment = "스크랩이 설정 되었습니다";
		}

		return comment;




	}

	public CommentsDTO getCommentListByPostId(Long userId, Long postId) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		PostEntity post = postRepository.findPostEntityById(postId)
			.orElseThrow(()->new BaseException(BaseResponseStatus.POST_NO_EXIST));

		List<CommentInfoDTO> commentInfoDTOList = boardRepository.findCommentsByPost(postId);
		CommentsDTO commentsDTO = new CommentsDTO();

		if(commentInfoDTOList.size() == 0){
			return commentsDTO;
		}

		commentsDTO.setCommentInfoDTOList(commentInfoDTOList);

		return commentsDTO;

	}


	public List<BoardResponseDTO> getHotBoard(Long likeCount) {

		// 유효성 검사
		if (likeCount == null || likeCount < 0) {
			throw new IllegalArgumentException("likeCount must be a non-negative number and cannot be null.");
		}

		List<PostEntity> hotPosts = boardRepository.selectByLike(likeCount);

		return hotPosts.stream()
				.map(post -> new BoardResponseDTO(
						post.getId(),
						post.getTitle(),
						post.getUrl(),
						post.getLikeCount(),
						post.getCommentCount()
				))
				.collect(Collectors.toList());    }

  
	public List<SearchHistoryDTO> getUserSearchHistory(Long userId) {
		return searchHistoryRepository.findSearchHistoryEntitiesByUser_Id(userId).stream()
				.map(this:: convertToSearchHistoryDTO)
				.collect(Collectors.toList());
	}

	private SearchHistoryDTO convertToSearchHistoryDTO(SearchHistoryEntity searchHistoryEntity) {
		SearchHistoryDTO searchHistoryDTO = new SearchHistoryDTO();

		searchHistoryDTO.setId((searchHistoryEntity.getId()));
		searchHistoryDTO.setContent(searchHistoryEntity.getContents());
		searchHistoryDTO.setUserId(searchHistoryEntity.getUser().getId());

		return searchHistoryDTO;
	}


}
