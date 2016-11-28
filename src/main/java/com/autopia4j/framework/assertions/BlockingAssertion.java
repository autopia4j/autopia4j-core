package com.autopia4j.framework.assertions;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.core.FrameworkParameters;
import com.autopia4j.framework.reporting.Report;
import com.autopia4j.framework.reporting.Status;

public class BlockingAssertion {
	
	private final Report report;
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
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
	
	public void assertTrue(Boolean condition, String message, Boolean stopExecution) {
		frameworkParameters.setStopExecution(!condition && stopExecution);
		this.assertTrue(condition, message);
	}
	
	public void assertFalse(Boolean condition, String message) {
		if(!condition) {
			report.updateTestLog(message, "Validation returned [false] as expected", Status.PASS, true);
		} else {
			throw new AutopiaException(message, "Expected [false] but found [true]");
		}
	}
	
	public void assertFalse(Boolean condition, String message, Boolean stopExecution) {
		frameworkParameters.setStopExecution(condition && stopExecution);
		this.assertFalse(condition, message);
	}
}