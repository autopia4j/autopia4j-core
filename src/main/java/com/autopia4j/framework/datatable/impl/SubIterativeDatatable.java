package com.autopia4j.framework.datatable.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.datatable.BaseDatatable;
import com.autopia4j.framework.utils.ExcelDataAccess;

/**
 * Class that implements a sub-iterative datatable, enabling 2 levels of data to be mapped to each test case
 * @author vj
 */
public class SubIterativeDatatable extends BaseDatatable {
	private final Logger logger = LoggerFactory.getLogger(SubIterativeDatatable.class);
	
	private int currentIteration = 0;
	private int currentSubIteration = 0;
	
	
	/**
	 * Constructor to initialize the {@link SubIterativeDatatable} object
	 * @param datatablePath The path where the datatable is stored
	 * @param datatableName The name of the datatable file
	 */
	public SubIterativeDatatable(String datatablePath, String datatableName) {
		super(datatablePath, datatableName);
	}
	
	/**
	 * Function to get the Iteration being executed currently
	 * @return The Iteration being executed currently
	 */
	public int getCurrentIteration() {
		return currentIteration;
	}
	
	/**
	 * Function to get the Sub-Iteration being executed currently
	 * @return The Sub-Iteration being executed currently
	 */
	public int getCurrentSubIteration() {
		return currentSubIteration;
	}
	
	@Override
	public void setCurrentRow(String currentTestcase) {
		String errorMessage = "setCurrentRow(): Missing arguments 'currentIteration' & 'currentSubIteration'!";
		logger.error(errorMessage);
		throw new AutopiaException(errorMessage);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration) {
		String errorMessage = "setCurrentRow(): Missing argument 'currentSubIteration'!";
		logger.error(errorMessage);
		throw new AutopiaException(errorMessage);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration) {
		this.currentTestcase = currentTestcase;
		this.currentIteration = currentIteration;
		this.currentSubIteration = currentSubIteration;
		
		logger.debug("Setting current row: " + currentTestcase + ", " + currentIteration + ", " + currentSubIteration);
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
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
									"of the test case \"" + currentTestcase + "\"" +
									"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The sub iteration number \"" + currentSubIteration + "\"" +
									"under iteration number \"" + currentIteration + "\"" +
									"of the test case \"" + currentTestcase + "\"" +
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
		if(currentIteration == 0) {
			String errorMessage = "The currentIteration is not set!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		if(currentSubIteration == 0) {
			String errorMessage = "The currentSubIteration is not set!";
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
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The sub iteration number \"" + currentSubIteration + "\"" +
										"under iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		synchronized (SubIterativeDatatable.class) {
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
		rowNum = expectedResultsAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
									"of the test case \"" + currentTestcase + "\"" +
									"is not found in the parametrized checkpoints sheet!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		rowNum = expectedResultsAccess.getRowNum(Integer.toString(currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The sub iteration number \"" + currentSubIteration + "\"" +
									"under iteration number \"" + currentIteration + "\"" +
									"of the test case \"" + currentTestcase + "\"" +
									"is not found in the parametrized checkpoints sheet!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		return expectedResultsAccess.getValue(rowNum, fieldName);
	}
}