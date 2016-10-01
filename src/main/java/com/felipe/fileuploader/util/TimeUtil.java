package com.felipe.fileuploader.util;

public class TimeUtil {

	public static Long extractSecondsFromTimestamps(Long timestamp2, Long timestamp1){
		return (timestamp2-timestamp1) / 1000;
	}
	
}
