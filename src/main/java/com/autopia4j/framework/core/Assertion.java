package com.autopia4j.framework.core;

import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

public class Assertion {
	
	private final Report report;
	
	public Assertion(Report report) {
		this.report = report;
	}
	
	public void assertTrue(Boolean condition, String message) {
		if(condition) {
			report.updateTestLog(message, "Condition [true] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [true] but found [false]", Status.FAIL, true);
		}
	}
	
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Condition [false] as expected", Status.PASS, true);
		} else {
			report.updateTestLog(message, "Expected [false] but found [true]", Status.FAIL, true);
		}
	}
}