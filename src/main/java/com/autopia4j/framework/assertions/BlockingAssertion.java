package com.autopia4j.framework.assertions;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.core.FrameworkParameters;
import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

/**
 * Blocking Assertion class for autopia4j
 * Assertions within this class block the test execution on failures
 * @author vj
 */
public class BlockingAssertion {
	
	private final Report report;
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	/**
	 * Constructor to initialize the {@link BlockingAssertion} object
	 * @param report The {@link Report} object
	 */
	public BlockingAssertion(Report report) {
		this.report = report;
	}
	
	/**
	 * Function to assert that a given condition is true, and block the test execution if false
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 */
	public void assertTrue(Boolean condition, String message) {
		if(condition) {
			report.updateTestLog(message, "Validation returned [true] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [true] but found [false]");
		}
	}
	
	/**
	 * Function to assert that a given condition is true, and block the test execution if false
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 * @param stopExecution Boolean value to indicate whether to abort execution of all subsequent tests on failure
	 */
	public void assertTrue(Boolean condition, String message, Boolean stopExecution) {
		frameworkParameters.setStopExecution(!condition && stopExecution);
		this.assertTrue(condition, message);
	}
	
	/**
	 * Function to assert that a given condition is false, and block the test execution if true
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 */
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Validation returned [false] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [false] but found [true]");
		}
	}
	
	/**
	 * Function to assert that a given condition is false, and block the test execution if true
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 * @param stopExecution Boolean value to indicate whether to abort execution of all subsequent tests on failure
	 */
	public void assertFalse(Boolean condition, String message, Boolean stopExecution) {
		frameworkParameters.setStopExecution(condition && stopExecution);
		this.assertFalse(condition, message);
	}
	
	/**
	 * Function to assert that the specified actual value equals the expected value, and block the test execution if not
	 * @param actual The object representing the actual value
	 * @param expected The object representing the expected value
	 * @param message The validation message to be reported
	 */
	public void assertEquals(Object actual, Object expected, String message) {
		if(actual.equals(expected)) {
			report.updateTestLog(message, "Validation returned [" + expected + "] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [" + expected + "] but found [" + actual + "]");
		}
	}
	
	/**
	 * Function to assert that the specified actual value equals the expected value, and block the test execution if not
	 * @param actual The object representing the actual value
	 * @param expected The object representing the expected value
	 * @param message The validation message to be reported
	 * @param stopExecution Boolean value to indicate whether to abort execution of all subsequent tests on failure
	 */
	public void assertEquals(Object actual, Object expected, String message, Boolean stopExecution) {
		frameworkParameters.setStopExecution((actual!=expected) && stopExecution);
		this.assertEquals(actual, expected, message);
	}
}