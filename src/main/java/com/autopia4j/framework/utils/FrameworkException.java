package com.autopia4j.framework.utils;


/**
 * Exception class for the framework
 * @author vj
 */
@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {
	private final String errorName;
	
	
	/**
	 * Constructor to initialize the exception from the framework
	 * @param errorDescription The Exception message to be thrown
	 */
	public FrameworkException(String errorDescription) {
		super(errorDescription);
		this.errorName = "Error";
	}
	
	/**
	 * Constructor to initialize the exception from the framework
	 * @param errorName The step name for the error
	 * @param errorDescription The Exception message to be thrown
	 */
	public FrameworkException(String errorName, String errorDescription) {
		super(errorDescription);
		this.errorName = errorName;
	}
	
	/**
	 * Function to get the error name
	 * @return The error name
	 */
	public String getErrorName() {
		return errorName;
	}
}