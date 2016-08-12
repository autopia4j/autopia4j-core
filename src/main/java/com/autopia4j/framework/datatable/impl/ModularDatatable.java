package com.autopia4j.framework.datatable.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.datatable.DataTableType;
import com.autopia4j.framework.utils.ExcelDataAccess;
import com.autopia4j.framework.utils.FrameworkException;
import com.autopia4j.framework.utils.Util;

/**
 * Class to encapsulate the datatable related functions of the framework
 * @author vj
 */
public class ModularDatatable implements DataTableType {
	private final Logger logger = LoggerFactory.getLogger(ModularDatatable.class);
	private final String datatablePath;
	private final String datatableName;
	private String dataReferenceIdentifier = "#";
	
	private String currentTestcase;
	private int currentIteration = 0;
	
	
	/**
	 * Constructor to initialize the {@link ModularDatatable} object
	 * @param datatablePath The path where the datatable is stored
	 * @param datatableName The name of the datatable file
	 */
	public ModularDatatable(String datatablePath, String datatableName) {
		this.datatablePath = datatablePath;
		this.datatableName = datatableName;
		
		logger.info("Initializing datatable @ " + datatablePath +
						Util.getFileSeparator() + datatableName + ".xls");
	}
	
	@Override
	public void setDataReferenceIdentifier(String dataReferenceIdentifier) {
		if (dataReferenceIdentifier.length() != 1) {
			String errorMessage = "The data reference identifier must be a single character!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		
		this.dataReferenceIdentifier = dataReferenceIdentifier;
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration) {
		this.currentTestcase = currentTestcase;
		this.currentIteration = currentIteration;
		
		logger.debug("Setting current row: " + currentTestcase + ", " + currentIteration);
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration) {
		String errorMessage = "setCurrentRow(): Unrecognized argument 'currentSubIteration'!";
		logger.error(errorMessage);
		throw new FrameworkException(errorMessage);
	}
	
	@Override
	public int getCurrentIteration() {
		return currentIteration;
	}
	
	private void checkPreRequisites() {
		if(currentTestcase == null) {
			String errorMessage = "The currentTestCase is not set!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		if(currentIteration == 0) {
			String errorMessage = "The currentIteration is not set!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
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
			throw new FrameworkException(errorMessage);
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		
		String dataValue = testDataAccess.getValue(rowNum, fieldName);
		
		if(dataValue.startsWith(dataReferenceIdentifier)) {
			dataValue = getCommonData(fieldName, dataValue);
		}
		
		return dataValue;
	}
	
	private String getCommonData(String fieldName, String dataValue) {
		ExcelDataAccess commonDataAccess = new ExcelDataAccess(datatablePath, "Common Testdata");
		commonDataAccess.setDatasheetName("Common_Testdata");
		
		String dataReferenceId = dataValue.split(dataReferenceIdentifier)[1];
		
		int rowNum = commonDataAccess.getRowNum(dataReferenceId, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			String errorMessage = "The common test data row identified by \"" + dataReferenceId + "\"" +
										"is not found in the common test data sheet!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		
		return commonDataAccess.getValue(rowNum, fieldName);
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
			throw new FrameworkException(errorMessage);
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		
		synchronized(ModularDatatable.class) {
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
			throw new FrameworkException(errorMessage);
		}
		rowNum = expectedResultsAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			String errorMessage = "The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the parametrized checkpoints sheet!";
			logger.error(errorMessage);
			throw new FrameworkException(errorMessage);
		}
		
		return expectedResultsAccess.getValue(rowNum, fieldName);
	}
}