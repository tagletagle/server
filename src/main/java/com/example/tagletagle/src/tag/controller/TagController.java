package com.example.tagletagle.src.tag.controller;

import com.example.tagletagle.base.BaseException;
import com.example.tagletagle.base.BaseResponse;
import com.example.tagletagle.base.BaseResponseStatus;
import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.tag.dto.TagInterestsDTO;
import com.example.tagletagle.src.tag.dto.TagResponseDTO;
import com.example.tagletagle.src.tag.service.TagService;
import com.example.tagletagle.utils.SecurityUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    //최근 사용한 태그 목록 불러오기
    @GetMapping("/api/user/tag/recent")
    public ResponseEntity<List<TagDTO>> getRecentTags(){
        List<TagDTO> recentTags = tagService.getRecentTags();
        return ResponseEntity.ok(recentTags);
    }

    @GetMapping("/api/tag/fullList")
    @Operation(summary = "전체 태그 목록 조회 api - 윤아", description = "현재 선택 가능한 모든 태그 리스트를 보여주는 api 입니다", responses = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
    })
    public ResponseEntity<BaseResponse<List<TagDTO>>> getFullTagList() {

        try{
            Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));

            return ResponseEntity.ok(new BaseResponse<>(tagService.findAllTag()));

        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
        }

    }

    @GetMapping("/api/tag/interests")
    @Operation(summary = "관심있는 태그 목록 조회 api - 윤아", description = "url로 user_id를 받아 해당 user의 관심있는 태그 목록을 조회하는 api입니다", responses = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "500", description = "로그인이 필요한 서비스 입니다")
    })
    public ResponseEntity<BaseResponse<List<TagInterestsDTO>>> getFollowingList(@RequestParam Long UserId){

        try{
            Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUIRED_LOGIN));
            return ResponseEntity.ok(new BaseResponse<>(tagService. getUserTagInterests(UserId)));
        }catch (BaseException e){
            HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body(new BaseResponse<>(e.getStatus()));
        }

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
