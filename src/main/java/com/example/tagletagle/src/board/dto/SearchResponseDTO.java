package com.example.tagletagle.src.board.dto;

import java.util.List;

import com.example.tagletagle.src.tag.dto.TagDTO;
import com.example.tagletagle.src.user.dto.UserProfileResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class SearchResponseDTO {
    private List<TagDTO> tags;
    private List<UserProfileResponseDTO> users;
}
