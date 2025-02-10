package com.example.tagletagle.src.tag.controller;

import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.dto.TagResponseDTO;
import com.example.tagletagle.src.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/user/tag/fullList")
    public List<TagDTO> getFullTagList() {
        return tagService.findAllTag();

    }

    //최근 사용한 태그 목록 불러오기
    @GetMapping("/api/user/tag/recent")
    public ResponseEntity<List<TagDTO>> getRecentTags(){
        List<TagDTO> recentTags = tagService.getRecentTags();
        return ResponseEntity.ok(recentTags);
    }

    //태그 취향정보 입력
    //[o] 예외처리
    @PostMapping("/api/user/tag/register")
    public ResponseEntity<String> registrateInterestTagList(@RequestBody TagResponseDTO request) {
        Long userId = request.getUserId();
        List<Long> interestTagId = request.getInterestTagId();

        if (userId == null || interestTagId == null || interestTagId.isEmpty()) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        tagService.saveTagsById(userId, interestTagId);

        return ResponseEntity.ok("태그가 성공적으로 등록되었습니다.");
    }

}
