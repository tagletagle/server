package com.example.tagletagle.src.user.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBasicInfoDTO {

	@Schema(description = "닉네임", nullable = false, example = "눈사람")
	@NotBlank
	private String nickname;

	@Schema(description = "자기소개", nullable = false, example = "여행, 음식을 좋아하는 인하대 학생입니다")
	private String description;

	@Schema(description = "나이", nullable = false, example = "2001-12-13")
	private LocalDate birthDate;

	@Schema(description = "성별", nullable = false, example = "M", allowableValues = {"M", "W"})
	@NotNull
	private Character gender;

}
