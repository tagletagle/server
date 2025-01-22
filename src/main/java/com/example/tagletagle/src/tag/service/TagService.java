package com.example.tagletagle.src.tag.service;

import com.example.tagletagle.src.tag.entity.TagEntity;
import com.example.tagletagle.src.tag.repository.TagRepository;
import com.example.tagletagle.src.tag.dto.TagDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
        private final TagRepository tagRepository;
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



    }
