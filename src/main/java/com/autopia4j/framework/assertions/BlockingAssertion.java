package com.autopia4j.framework.assertions;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

public class BlockingAssertion {
	
	private final Report report;
	
	public BlockingAssertion(Report report) {
		this.report = report;
	}
	
	public void assertTrue(Boolean condition, String message) {
		if(condition) {
			report.updateTestLog(message, "Validation returned [true] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [true] but found [false]");
		}
	}
	
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Validation returned [false] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [false] but found [true]");
		}
	}
}