package com.autopia4j.framework.assertions;

import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

public class BasicAssertion {
	
	private final Report report;
	
	public BasicAssertion(Report report) {
		this.report = report;
	}
	
	public void assertTrue(Boolean condition, String message) {
		if(condition) {
			report.updateTestLog(message, "Validation returned [true] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [true] but found [false]", Status.FAIL, true);
		}
	}
	
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Validation returned [false] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [false] but found [true]", Status.FAIL, true);
		}
	}
}