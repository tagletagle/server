package com.example.tagletagle.src.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "좋아요 유저 정보를 담은 DTO")
@NoArgsConstructor
public class LikedUsersInfoDTO {

    private Long userId;
    private String nickName;
    private String userProfileImgUrl;

    public LikedUsersInfoDTO(Long userId, String nickName, String userProfileImgUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.userProfileImgUrl = userProfileImgUrl;
    }
}
