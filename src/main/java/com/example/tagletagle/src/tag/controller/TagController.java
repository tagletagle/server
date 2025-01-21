package com.example.tagletagle.src.tag.controller;

import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.tag.service.TagService;
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
}
