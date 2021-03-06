package com.autopia4j.framework.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;


/**
 * Class to encapsulate utility functions of the framework
 * @author vj
 */
public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	private Util() {
		// To prevent external instantiation of this class
	}
	
	/**
	 * Function to get the separator string to be used for directories and files based on the current OS
	 * @return The file separator string
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * Function to return the current time
	 * @return The current time
	 * @see #getCurrentFormattedTime(String)
	 */
	public static Date getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
	
	/**
	 * Function to return the current time, formatted as per the DateFormatString setting
	 * @param dateFormatString The date format string to be applied
	 * @return The current time, formatted as per the date format string specified
	 * @see #getCurrentTime()
	 * @see #getFormattedTime(Date, String)
	 */
	public static String getCurrentFormattedTime(String dateFormatString) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	 * Function to format the given time variable as specified by the DateFormatString setting
	 * @param time The date/time variable to be formatted
	 * @param dateFormatString The date format string to be applied
	 * @return The specified date/time, formatted as per the date format string specified
	 * @see #getCurrentFormattedTime(String)
	 */
	public static String getFormattedTime(Date time, String dateFormatString) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(time);
	}
	
	/**
	 * Function to get the time difference between 2 {@link Date} variables in minutes/seconds format
	 * @param startTime The start time
	 * @param endTime The end time
	 * @return The time difference in terms of hours, minutes and seconds
	 */
	public static String getTimeDifference(Date startTime, Date endTime) {
		long timeDifferenceSeconds = (endTime.getTime() - startTime.getTime()) / 1000;	// to convert from milliseconds to seconds
		long timeDifferenceMinutes = timeDifferenceSeconds / 60;
		
		String timeDifferenceDetailed;
		if (timeDifferenceMinutes >= 60) {
			long timeDifferenceHours = timeDifferenceMinutes / 60;
			
			timeDifferenceDetailed = Long.toString(timeDifferenceHours) + " hour(s), "
									+ Long.toString(timeDifferenceMinutes % 60) + " minute(s), "
									+ Long.toString(timeDifferenceSeconds % 60) + " second(s)";
		} else {
			timeDifferenceDetailed = Long.toString(timeDifferenceMinutes) + " minute(s), "
									+ Long.toString(timeDifferenceSeconds % 60) + " second(s)";
		}
		
		return timeDifferenceDetailed;
	}
	
	/**
	 * Function to convert the first character of a given string to uppercase
	 * @param input The input string
	 * @return The input string with its first character converted to uppercase
	 */
	public static String capitalizeFirstLetter(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	/**
	 * Function to convert the first character of a given string to lowercase
	 * @param input The input string
	 * @return The input string with its first character converted to lowercase
	 */
	public static String unCapitalizeFirstLetter(String input) {
		return input.substring(0, 1).toLowerCase() + input.substring(1);
	}
	
	/**
	 * Function to initiate a {@link URL} object based on the given URL string
	 * @param urlAsString The URL string
	 * @return The {@link URL} object based on the given URL string
	 */
	public static URL getUrl(String urlAsString) {
		URL url;
		try {
			url = new URL(urlAsString);
		} catch (MalformedURLException e) {
			String errorDescription = "The specified URL string is malformed";
			logger.error(errorDescription, e);
			throw new AutopiaException(errorDescription);
		}
		return url;
	}
}