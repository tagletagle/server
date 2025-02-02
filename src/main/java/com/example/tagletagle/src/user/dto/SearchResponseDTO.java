package com.example.tagletagle.src.user.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.tagletagle.src.tag.dto.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class SearchResponseDTO {
    private List<TagDTO> tags;
    private List<UserProfileResponseDTO> users;
}
