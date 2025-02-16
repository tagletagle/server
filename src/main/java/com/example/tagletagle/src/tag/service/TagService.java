package com.example.tagletagle.src.tag.service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.board.entity.PostScrapEntity;
import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.entity.TagRequestEntity;
import com.example.tagletagle.src.tag.entity.UserTagInterests;
import com.example.tagletagle.src.tag.repository.TagInterestsRepository;
import com.example.tagletagle.src.tag.repository.TagRepository;
import com.example.tagletagle.src.tag.repository.TagRequestRepository;
import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.user.dto.FollowsDTO;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
        private final TagRepository tagRepository;
        private final TagInterestsRepository tagInterestsRepository;
        private final UserRepository userRepository;
        private final TagRequestRepository tagRequestRepository;

        /*stream 사용해서 컬렉션 데이터를 변환
         * map은 요소를 다른 값으로 변환
         * collect 사용하면 스트림 요소 수집해서 컬렉션 생성*/
        public List<TagDTO> findAllTag() {
            return tagRepository.findAll().stream()
                    .map(this::convertToTagDTO)//TagDTO로 변환
                    .collect(Collectors.toList()); //결과를 List로 수집
        }

        //변환 위한 함수 선언
        private TagDTO convertToTagDTO(TagEntity tagEntity) {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setId(tagEntity.getId());
            tagDTO.setName(tagEntity.getName());
            return tagDTO;
        }

    public List<TagInterestsDTO> getUserTagInterests(Long user) {
        return tagInterestsRepository.findUserTagInterestsByUser(user).stream()
                .map(this::convertToTagInterestsDTO)
                .collect(Collectors.toList());
    }

    private TagInterestsDTO convertToTagInterestsDTO(UserTagInterests userTagInterests) {
        TagInterestsDTO tagInterestsDTO = new TagInterestsDTO();

        tagInterestsDTO.setId((userTagInterests.getId()));
        tagInterestsDTO.setUserId(userTagInterests.getUser().getId());
        tagInterestsDTO.setTagId(userTagInterests.getTag().getId());

        return  tagInterestsDTO;
    }

    public void tagRequest(TagDTO tagDTO) {

        TagRequestEntity tagRequest = new TagRequestEntity(tagDTO);
        tagRequestRepository.save(tagRequest);

    }

    /**
     * 최근 사용한 태그 목록 조회
     * @return List<TagResponse> 최근 사용한 태그 목록
     */
    public List<TagDTO> getRecentTags(){
        List<TagEntity> recentTags = tagRepository.findTop10ByOrderByUpdateAtDesc();

        // Entity를 DTO로 변환
        return recentTags.stream()
                .map(tag->new TagDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    public void saveTagsById(Long userId, List<Long> interestTagId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<UserTagInterests> userTagInterests = interestTagId.stream()
                .map(tagId -> {
                    TagEntity tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));

                    // user와 tag를 설정하기 위해 UserTagInterests 객체를 생성
                    UserTagInterests userTagInterest = new UserTagInterests();
                    userTagInterest.setUser(user); // setter로 user 설정
                    userTagInterest.setTag(tag);   // setter로 tag 설정
                    return userTagInterest; // 생성된 객체 반환
                })
                .collect(Collectors.toList());

        tagInterestsRepository.saveAll(userTagInterests);
    }

    @Transactional
    public String registerOrDelete(Long userId, List<Long> tagIds) {
        // 유저 조회
        UserEntity user = userRepository.findUserEntityByIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NO_EXIST));

        // 처리 결과 메시지를 저장할 리스트
        List<String> results = new ArrayList<>();

        for (Long tagId : tagIds) {
            // 태그 조회
            TagEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NO_EXIST));

            // 해당 유저가 태그를 등록했는지 확인
            boolean isUser = tagInterestsRepository.existsByUserAndTag(user, tag);

            if (isUser) {
                // 태그가 이미 등록된 경우 → 삭제
                tagInterestsRepository.deleteUserTagInterestsByUserAndTag(user, tag);
                results.add("태그 ID " + tagId + "이 해제되었습니다.");
            } else {
                // 태그가 없는 경우 → 추가
                UserTagInterests userTagInterests = new UserTagInterests();
                userTagInterests.setUser(user);
                userTagInterests.setTag(tag);
                tagInterestsRepository.save(userTagInterests);
                results.add("태그 ID " + tagId + "이 설정되었습니다.");
            }
        }

        // 결과 메시지를 하나의 문자열로 합쳐서 반환
        return String.join("\n", results);
    }
}
