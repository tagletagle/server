package com.example.tagletagle.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {
    public static String getTimeLine(LocalDateTime writingTime){

		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(writingTime, now);

		long seconds = duration.getSeconds();
		long minutes = duration.toMinutes();
		long hours = duration.toHours();
		long days = duration.toDays();
		int months = now.getMonthValue() - writingTime.getMonthValue()
			+ (now.getYear() - writingTime.getYear()) * 12;
		int years = now.getYear() - writingTime.getYear();


		if (seconds < 60) {
			return seconds + "초 전";
		} else if (minutes < 60) {
			return minutes + "분 전";
		} else if (hours < 24) {
			return hours + "시간 전";
		} else if (days < 30) {  // 30일 미만이면 "n일 전" 표시
			return days + "일 전";
		} else if (months < 12) {  // 12개월 미만이면 "n개월 전" 표시
			return months + "개월 전";
		} else {
			return years + "년 전";
		}

    }

}
