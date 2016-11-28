package com.autopia4j.framework.assertions;

import org.testng.Assert;

import com.autopia4j.framework.assertions.BasicAssertion;
import com.autopia4j.framework.core.FrameworkParameters;
import com.autopia4j.framework.reporting.Report;

/**
 * Extension of the autopia4j Basic Assertion class that wraps TestNG based assertions<br>
 * Assertions within this class block the test execution on failures
 * @author vj
 */
public class TestNgWrappedAssertion extends BasicAssertion {
	
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	/**
	 * Constructor to initialize the {@link TestNgWrappedAssertion} object
	 * @param report The {@link Report} object
	 */
	public TestNgWrappedAssertion(Report report) {
		super(report);
	}
	
	/**
	 * Function to assert that a given condition is true, and block the test execution if false
	 * @param condition The condition to be validated
	 * @param message The validation message to be reported
	 */
	@Override
	public void assertTrue(Boolean condition, String message) {
		super.assertTrue(condition, message);
		Assert.assertTrue(condition, message);
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
	@Override
	public void assertFalse(Boolean condition, String message) {
		super.assertFalse(condition, message);
		Assert.assertFalse(condition, message);
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
	@Override
	public void assertEquals(Object actual, Object expected, String message) {
		super.assertEquals(actual, expected, message);
		Assert.assertEquals(actual, expected, message);
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