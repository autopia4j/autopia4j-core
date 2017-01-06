package com.autopia4j.framework.datatable.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.datatable.BaseDatatable;
import com.autopia4j.framework.utils.ExcelDataAccess;

/**
 * Class that implements a non-iterative datatable, enabling a single row of data to be mapped to each test case
 * @author vj
 */
public class NonIterativeDatatable extends BaseDatatable {
	private final Logger logger = LoggerFactory.getLogger(NonIterativeDatatable.class);
	
	
	/**
	 * Constructor to initialize the {@link NonIterativeDatatable} object
	 * @param datatablePath The path where the datatable is stored
	 * @param datatableName The name of the datatable file
	 */
	public NonIterativeDatatable(String datatablePath, String datatableName) {
		super(datatablePath, datatableName);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase) {
		this.currentTestcase = currentTestcase;
		
		logger.debug("Setting current row: " + currentTestcase);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration) {
		String errorMessage = "setCurrentRow(): Unrecognized integer argument!";
		logger.error(errorMessage);
		throw new AutopiaException(errorMessage);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration) {
		String errorMessage = "setCurrentRow(): Unrecognized integer arguments!";
		logger.error(errorMessage);
		throw new AutopiaException(errorMessage);
	}
	
	@Override
	public String getData(String datasheetName, String fieldName) {
		checkPreRequisites();
		
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, datatableName);
		testDataAccess.setDatasheetName(datasheetName);
		
		int rowNum = testDataAccess.getRowNum(currentTestcase, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			String errorMessage = "The test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		String dataValue = testDataAccess.getValue(rowNum, fieldName);
		
		if(dataValue.startsWith(dataReferenceIdentifier)) {
			dataValue = getCommonData(fieldName, dataValue);
		}
		
		return dataValue;
	}
	
	private void checkPreRequisites() {
		if(currentTestcase == null) {
			String errorMessage = "The currentTestCase is not set!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
	}
	
	@Override
	public void putData(String datasheetName, String fieldName, String dataValue) {
		checkPreRequisites();
		
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, datatableName);
		testDataAccess.setDatasheetName(datasheetName);
		
		int rowNum = testDataAccess.getRowNum(currentTestcase, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			String errorMessage = "The test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		synchronized(NonIterativeDatatable.class) {
			testDataAccess.setValue(rowNum, fieldName, dataValue);
		}
	}
	
	@Override
	public String getExpectedResult(String fieldName) {
		checkPreRequisites();
		
		ExcelDataAccess expectedResultsAccess = new ExcelDataAccess(datatablePath, datatableName);
		expectedResultsAccess.setDatasheetName("Parametrized_Checkpoints");
		
		int rowNum = expectedResultsAccess.getRowNum(currentTestcase, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			String errorMessage = "The test case \"" + currentTestcase + "\"" +
										"is not found in the parametrized checkpoints sheet!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		return expectedResultsAccess.getValue(rowNum, fieldName);
	}
}