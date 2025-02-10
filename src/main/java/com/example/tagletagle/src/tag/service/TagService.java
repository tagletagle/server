package com.example.tagletagle.src.tag.service;

import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.entity.UserTagInterests;
import com.example.tagletagle.src.tag.repository.TagInterestsRepository;
import com.example.tagletagle.src.tag.repository.TagRepository;
import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.user.dto.FollowsDTO;
import com.example.tagletagle.src.user.entity.FollowsEntity;
import com.example.tagletagle.src.user.entity.UserEntity;
import com.example.tagletagle.src.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
        private final TagRepository tagRepository;
        private final TagInterestsRepository tagInterestsRepository;
        private final UserRepository userRepository;
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

    //[o]findAllById 구현
    //[o]insertUserInterestTag 구현
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
}
