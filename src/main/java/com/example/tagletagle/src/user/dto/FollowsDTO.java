package com.example.tagletagle.src.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class FollowsDTO {
    private Long id;
    private Long followerId; //팔로워(=팔로잉 하는 사람)
    private Long followingId; //팔로잉 하는 사람
    private String followingNickname;
    private String followingProfile;

    private String followerNickname;
    private String followerProfile;
}
