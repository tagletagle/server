package com.example.tagletagle.src.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.board.dto.CreatePostDTO;
import com.example.tagletagle.src.board.dto.PostInfoDTO;
import com.example.tagletagle.src.board.dto.PostsDTO;
import com.example.tagletagle.src.board.entity.PostEntity;
import com.example.tagletagle.src.board.repository.BoardRepository;
import com.example.tagletagle.src.board.repository.PostRepository;
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

	@Transactional
	public void createPost(Long userId, CreatePostDTO createPostDTO) {

		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		PostEntity post = new PostEntity(createPostDTO, user);
		postRepository.save(post);

		List<Long> tagList = createPostDTO.getTagIdList();

		if(tagList.size() == 0){
			//예외 처리
			return;
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

	public PostsDTO getPostsByUserWithTag(Long userId, Long authorId, String tagName) {
		UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.USER_NO_EXIST));

		UserEntity author = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
			.orElseThrow(()->new BaseException(BaseResponseStatus.AUTHOR_NO_EXIST));

		PostsDTO postsDTO = new PostsDTO();
		List<PostInfoDTO> postInfoDTOList = boardRepository.findPostsByAuthorAndUserWithTag(authorId, userId, tagName);
		if(postInfoDTOList.size() == 0){
			return postsDTO;
		}


		postsDTO.setPostInfoDTOList(postInfoDTOList);

		return postsDTO;

	}
}
