package com.example.tagletagle.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.tagletagle.src.board.dto.TagInfoDTO;

public class TagUtils {

	public static List<TagInfoDTO> parseTags(String tagIds, String tagNames) {
		// ID와 Name을 콤마로 분리
		String[] idArray = tagIds.split(",");
		String[] nameArray = tagNames.split(",");

		// ID와 Name의 길이가 일치하지 않으면 예외 처리
		if (idArray.length != nameArray.length) {
			throw new IllegalArgumentException("tagIds와 tagNames의 길이가 일치하지 않습니다.");
		}

		// 결과 리스트 생성
		List<TagInfoDTO> tags = new ArrayList<>();
		for (int i = 0; i < idArray.length; i++) {
			// 공백 제거 및 매핑
			Long id = Long.parseLong(idArray[i].trim());
			String name = nameArray[i].trim();
			tags.add(new TagInfoDTO(id, name));
		}

		return tags;
	}
}
