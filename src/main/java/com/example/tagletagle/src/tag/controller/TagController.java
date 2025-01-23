package com.example.tagletagle.src.tag.controller;

import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.service.TagService;
import com.example.tagletagle.src.user.dto.FollowsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tag/fullList")
    public List<TagDTO> getFullTagList() {
        return tagService.findAllTag();

    }

    @GetMapping("/api/tag/interests")
    public List<TagInterestsDTO> getFollowingList(@RequestParam Long UserId){

        return tagService. getUserTagInterests(UserId);

    }
}
