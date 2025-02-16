package com.example.tagletagle.meta;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jakarta.annotation.PreDestroy;

@Service
public class WebDriverPool {
	private static final int POOL_SIZE = 3;
	private static final BlockingQueue<WebDriver> pool = new LinkedBlockingQueue<>();

	static {
		for (int i = 0; i < POOL_SIZE; i++) {
			pool.offer(createNewDriver());
		}
	}

	private static WebDriver createNewDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-popup-blocking"); // 팝업안띄움
		options.addArguments("--headless"); // 브라우저 창 숨김
		options.addArguments("--disable-gpu"); // gpu 비활성화
		options.addArguments("--disable-dev-shm-usage"); // Docker 환경에서 유용
		options.addArguments("--no-sandbox");
		options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
		options.addArguments("--disable-javascript");


		options.setPageLoadTimeout(Duration.ofSeconds(10));
		return new ChromeDriver(options);
	}

	public static WebDriver getDriver() throws InterruptedException {
		WebDriver driver = pool.take();
		validateAndReplaceDriver(driver);
		return driver;
	}

	public static void releaseDriver(WebDriver driver) {
		pool.offer(driver);
	}

	public static void replaceDriver(WebDriver driver) {
		driver.quit();
		pool.offer(createNewDriver());
	}

	private static void validateAndReplaceDriver(WebDriver driver) {
		try {
			driver.getTitle();  // 정상 동작 여부 확인
		} catch (Exception e) {
			replaceDriver(driver);
		}
	}

	@PreDestroy
	public void cleanUpDrivers() {
		while (!pool.isEmpty()) {
			WebDriver driver = pool.poll();
			if (driver != null) {
				driver.quit();
			}
		}
	}
}
