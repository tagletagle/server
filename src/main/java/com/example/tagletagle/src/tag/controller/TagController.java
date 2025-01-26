package com.example.tagletagle.src.tag.controller;

import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    //최근 사용한 태그 목록 불러오기
//    @GetMapping("/api/user/tag/recent")
//    public ResponseEntity<List<TagDTO>> getRecentTags(){
//        List<TagDTO> recentTags = tagService.getRecentTags();
//        return ResponseEntity.ok(recentTags);
//    }

}
