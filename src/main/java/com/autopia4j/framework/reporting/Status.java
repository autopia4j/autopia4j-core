package com.autopia4j.framework.reporting;


/**
 * Enumeration to represent the status of the current test step
 * @author Cognizant
 */
public enum Status {
	/**
	 * Indicates that the outcome of a verification was not successful
	 */
	FAIL,
	/**
	 * Indicates a warning message
	 */
	WARNING,
	/**
	 * Indicates that the outcome of a verification was successful
	 */
	PASS,
	/**
	 * Indicates a message that is logged into the results for informational purposes
	 */
	DONE;
}