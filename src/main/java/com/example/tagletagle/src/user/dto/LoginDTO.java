package com.example.tagletagle.src.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Object Mapper을 통해 json 받아올 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

	@NotBlank
	private String username;

}