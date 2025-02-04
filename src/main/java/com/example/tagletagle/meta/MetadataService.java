package com.example.tagletagle.meta;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;

@Service
public class MetadataService {

	// 메타데이터 가져오기 (네이버/티스토리 예외 처리 포함)
	/*public Map<String, String> fetchMetadata(String url) {
		WebDriver driver = null;
		Map<String, String> metadata = new HashMap<>();

		try {
			// ChromeDriver 자동 다운로드 설정
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-popup-blocking"); // 팝업안띄움
			options.addArguments("--headless"); // 브라우저 창 숨김
			options.addArguments("--disable-gpu"); // gpu 비활성화
			options.addArguments("--disable-dev-shm-usage"); // Docker 환경에서 유용
			options.addArguments("--no-sandbox");
			options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
			options.addArguments("--disable-javascript");

			driver = new ChromeDriver(options);
			WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
			driver.get(url);

			// 네이버 블로그 예외 처리
			if (url.contains("blog.naver.com")) {
				metadata = fetchNaverBlogMetadata(driver);
			}
			// 티스토리 예외 처리
			else if (url.contains("tistory.com")) {
				metadata = fetchTistoryMetadata(driver);
			}
			// 기본 Open Graph 처리
			else {
				metadata = fetchOpenGraphMetadata(driver);
			}
		} catch (Exception e) {
			e.printStackTrace();
			metadata.put("error", "Failed to fetch metadata: " + e.getMessage());
		} finally {
			if (driver != null) {
				driver.quit(); // 드라이버 종료
			}
		}

		return metadata;
	}*/
	public Map<String, String> fetchMetadata(String url) {
		WebDriver driver = null;
		Map<String, String> metadata = new HashMap<>();

		try {
			driver = WebDriverPool.getDriver(); // 풀에서 WebDriver 가져오기
			driver.get(url);

			if (url.contains("blog.naver.com")) {
				metadata = fetchNaverBlogMetadata(driver);
			} else if (url.contains("tistory.com")) {
				metadata = fetchTistoryMetadata(driver);
			} else {
				metadata = fetchOpenGraphMetadata(driver);
			}
		} catch (Exception e) {
			metadata.put("error", "Failed to fetch metadata: " + e.getMessage());
		} finally {
			if (driver != null) {
				WebDriverPool.releaseDriver(driver); // 사용 완료 후 다시 풀에 반환
			}
		}
		return metadata;
	}


	// 네이버 블로그 메타데이터 처리
	private Map<String, String> fetchNaverBlogMetadata(WebDriver driver) {
		Map<String, String> metadata = new HashMap<>();
		try {
			WebElement iframe = driver.findElement(By.tagName("iframe"));
			driver.switchTo().frame(iframe); // iframe 내부로 이동

			metadata.put("title", getMetaTag(driver, "og:title"));
			metadata.put("image", getMetaTag(driver, "og:image"));
			metadata.put("author", getMetaTag(driver, "article:author"));

			// 만약 author 태그가 없으면 기본 author 정보 검색
			if (metadata.get("author") == null) {
				metadata.put("author", getMetaTag(driver, "og:author"));
			}
		} catch (Exception e) {
			metadata.put("error", "Failed to fetch metadata from Naver blog: " + e.getMessage());
		}
		return metadata;
	}

	// 티스토리 메타데이터 처리
	private Map<String, String> fetchTistoryMetadata(WebDriver driver) {
		Map<String, String> metadata = new HashMap<>();
		try {
			metadata.put("title", getMetaTag(driver, "og:title"));
			metadata.put("image", getMetaTag(driver, "og:image"));
			metadata.put("author", getMetaTag(driver, "article:author"));

			// 만약 author 태그가 없으면 기본 author 정보 검색
			if (metadata.get("author") == null) {
				metadata.put("author", getMetaTag(driver, "og:author"));
			}
		} catch (Exception e) {
			metadata.put("error", "Failed to fetch metadata from Tistory: " + e.getMessage());
		}
		return metadata;
	}

	// Open Graph 태그 처리 (기본 메타데이터 처리)
	private Map<String, String> fetchOpenGraphMetadata(WebDriver driver) {
		Map<String, String> metadata = new HashMap<>();
		try {
			metadata.put("title", getMetaTag(driver, "og:title"));
			metadata.put("image", getMetaTag(driver, "og:image"));
			metadata.put("author", getMetaTag(driver, "article:author"));

			// 만약 author 태그가 없으면 추가 검색
			if (metadata.get("author") == null) {
				metadata.put("author", getMetaTag(driver, "og:author"));
			}
			if (metadata.get("author") == null) {
				metadata.put("author", getMetaTag(driver, "name=author"));
			}

			// 제목이 없을 경우 기본 <title> 태그 사용
			if (metadata.get("title") == null || metadata.get("title").isEmpty()) {
				metadata.put("title", driver.getTitle());
			}
		} catch (Exception e) {
			metadata.put("error", "Failed to fetch Open Graph metadata: " + e.getMessage());
		}
		return metadata;
	}

	// 메타 태그 읽기 유틸 메서드
	private String getMetaTag(WebDriver driver, String property) {
		try {
			WebElement metaTag = driver.findElement(By.cssSelector("meta[property='" + property + "']"));
			return metaTag.getAttribute("content");
		} catch (Exception e) {
			return null;
		}
	}
}
