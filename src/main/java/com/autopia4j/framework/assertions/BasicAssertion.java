package com.autopia4j.framework.assertions;

import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

/**
 * Basic Assertion class for autopia4j<br>
 * Assertions within this class do not block the test execution
 * @author vj
 */
public class BasicAssertion {
	
	private final Report report;
	
	/**
	 * Constructor to initialize the {@link BasicAssertion} object
	 * @param report The {@link Report} object
	 */
	public BasicAssertion(Report report) {
		this.report = report;
	}
	
	/**
	 * Function to assert that a given condition is true
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 */
	public void assertTrue(Boolean condition, String message) {
		if(condition) {
			report.updateTestLog(message, "Validation returned [true] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [true] but found [false]", Status.FAIL, true);
		}
	}
	
	/**
	 * Function to assert that a given condition is false
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 */
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Validation returned [false] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [false] but found [true]", Status.FAIL, true);
		}
	}
	
	/**
	 * Function to assert that the specified actual value equals the expected value
	 * @param actual The object representing the actual value
	 * @param expected The object representing the expected value
	 * @param message The validation message to be reported
	 */
	public void assertEquals(Object actual, Object expected, String message) {
		if(actual.equals(expected)) {
			report.updateTestLog(message, "Validation returned [" + expected + "] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [" + expected + "] but found [" + actual + "]", Status.FAIL, true);
		}
	}
}