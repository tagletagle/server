package com.example.tagletagle.meta;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

	private final SeleniumMetadataService seleniumMetadataService;
	private final JsoupMetadataService jsoupMetadataService;

	public MetadataController(SeleniumMetadataService seleniumMetadataService, JsoupMetadataService jsoupMetadataService) {

		this.seleniumMetadataService = seleniumMetadataService;
		this.jsoupMetadataService = jsoupMetadataService;
	}

	// 1️⃣ 단일 URL 메타데이터 가져오기
	@GetMapping()
	public ResponseEntity<Map<String, String>> getMetadata(@RequestParam("url") String url) {
		Map<String, String> metadata = seleniumMetadataService.fetchMetadata(url);
		return ResponseEntity.ok(metadata);
	}

	// 단일 URL 메타데이터를 비동기 방식으로 가져오기
	@GetMapping("/async")
	public ResponseEntity<CompletableFuture<Map<String, String>>> getMetadataAsync(@RequestParam("url") String url) {
		CompletableFuture<Map<String, String>> metadata = seleniumMetadataService.fetchMetadataAsync(url);
		System.out.println(metadata.join().get("title"));
		System.out.println(metadata.join().get("image"));


		return ResponseEntity.ok(metadata);
	}

	@GetMapping("/jsoup")
	public ResponseEntity<Map<String, String>> getJsoupMetadta(@RequestParam("url")String url){
		Map<String, String> metadata = jsoupMetadataService.fetchMetadata(url);


		return ResponseEntity.ok(metadata);
	}

/*	@PostMapping
	public ResponseEntity<Map<String, String>> getMetadata(@RequestBody Map<String, String> requestBody) {
		String url = requestBody.get("url"); // JSON에서 "url" 값을 추출
		if (url == null || url.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "URL is required"));
		}

		Map<String, String> metadata = metadataService.fetchMetadata(url);
		return ResponseEntity.ok(metadata);
	}*/

	// 2️⃣ 여러 개의 URL 메타데이터 가져오기 (멀티스레딩 활용)
	/*@PostMapping("/batch")
	public ResponseEntity<List<Map<String, String>>> getMultipleMetadata(@RequestBody List<String> urls) {
		List<Map<String, String>> metadataList = metadataService.fetchMultipleMetadata(urls);
		return ResponseEntity.ok(metadataList);
	}*/
}
