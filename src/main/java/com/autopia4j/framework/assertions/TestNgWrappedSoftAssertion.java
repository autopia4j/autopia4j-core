package com.autopia4j.framework.assertions;

import org.testng.asserts.SoftAssert;

import com.autopia4j.framework.assertions.BasicAssertion;
import com.autopia4j.framework.reporting.Report;

/**
 * Extension of the autopia4j Basic Assertion class that wraps TestNG based soft assertions<br>
 * Assertions within this class do not block the test execution on failures<br>
 * Remember to call the assertAll() method on the TestNg soft assertion object from the calling test
 * @author vj
 */
public class TestNgWrappedSoftAssertion extends BasicAssertion {
	
	private SoftAssert softly;
	
	/**
	 * Constructor to initialize the {@link TestNgWrappedSoftAssertion} object
	 * @param report The {@link Report} object
	 * @param softly The {@link SoftAssert} object
	 */
	public TestNgWrappedSoftAssertion(Report report, SoftAssert softly) {
		super(report);
		this.softly = softly;
	}
	
	@Override
	public void assertTrue(Boolean condition, String message) {
		super.assertTrue(condition, message);
		softly.assertTrue(condition, message);
	}
	
	@Override
	public void assertFalse(Boolean condition, String message) {
		super.assertFalse(condition, message);
		softly.assertFalse(condition, message);
	}
	
	@Override
	public void assertEquals(Object actual, Object expected, String message) {
		super.assertEquals(actual, expected, message);
		softly.assertEquals(actual, expected, message);
	}
}