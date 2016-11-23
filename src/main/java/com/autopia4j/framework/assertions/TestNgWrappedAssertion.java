package com.autopia4j.framework.assertions;

import org.testng.Assert;

import com.autopia4j.framework.assertions.BasicAssertion;
import com.autopia4j.framework.reporting.Report;

public class TestNgWrappedAssertion extends BasicAssertion {
	
	public TestNgWrappedAssertion(Report report) {
		super(report);
	}
	
	@Override
	public void assertTrue(Boolean condition, String message) {
		super.assertTrue(condition, message);
		Assert.assertTrue(condition, message);
	}
	
	@Override
	public void assertFalse(Boolean condition, String message) {
		super.assertFalse(condition, message);
		Assert.assertFalse(condition, message);
	}
}