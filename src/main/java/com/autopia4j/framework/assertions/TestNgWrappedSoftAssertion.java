package com.autopia4j.framework.assertions;

import org.testng.asserts.SoftAssert;

import com.autopia4j.framework.assertions.BasicAssertion;
import com.autopia4j.framework.reporting.Report;

public class TestNgWrappedSoftAssertion extends BasicAssertion {
	
	private SoftAssert softly;
	
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
}