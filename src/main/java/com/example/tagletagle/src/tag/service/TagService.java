package com.example.tagletagle.src.tag.service;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.config.Status;
import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.entity.TagRequestEntity;
import com.example.tagletagle.src.tag.entity.UserTagInterests;
import com.example.tagletagle.src.tag.repository.TagInterestsRepository;
import com.example.tagletagle.src.tag.repository.TagRepository;
import com.example.tagletagle.src.tag.repository.TagRequestRepository;
import com.example.tagletagle.src.tag.dto.TagDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
        private final TagRepository tagRepository;
        private final TagInterestsRepository tagInterestsRepository;
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




}
