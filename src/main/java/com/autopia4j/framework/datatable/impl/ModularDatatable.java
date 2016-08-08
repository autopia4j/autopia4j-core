package com.autopia4j.framework.datatable.impl;

import com.autopia4j.framework.datatable.DataTableType;
import com.autopia4j.framework.utils.ExcelDataAccess;
import com.autopia4j.framework.utils.FrameworkException;

/**
 * Class to encapsulate the datatable related functions of the framework
 * @author vj
 */
public class ModularDatatable implements DataTableType {
	private final String datatablePath, datatableName;
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
	}
	
	@Override
	public void setDataReferenceIdentifier(String dataReferenceIdentifier) {
		if (dataReferenceIdentifier.length() != 1) {
			throw new FrameworkException("The data reference identifier must be a single character!");
		}
		
		this.dataReferenceIdentifier = dataReferenceIdentifier;
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration) {
		this.currentTestcase = currentTestcase;
		this.currentIteration = currentIteration;
	}
	
	@Override
	public void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration) {
		throw new FrameworkException("Not applicable for modular framework datatable");
	}
	
	@Override
	public int getCurrentIteration() {
		return currentIteration;
	}
	
	private void checkPreRequisites() {
		if(currentTestcase == null) {
			throw new FrameworkException("CraftliteDataTable.currentTestCase is not set!");
		}
		if(currentIteration == 0) {
			throw new FrameworkException("CraftliteDataTable.currentIteration is not set!");
		}
	}
	
	@Override
	public String getData(String datasheetName, String fieldName) {
		checkPreRequisites();
		
		ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, datatableName);
		testDataAccess.setDatasheetName(datasheetName);
		
		int rowNum = testDataAccess.getRowNum(currentTestcase, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			throw new FrameworkException("The test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!");
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!");
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
			throw new FrameworkException("The common test data row identified by \"" + dataReferenceId + "\"" +
										"is not found in the common test data sheet!");
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
			throw new FrameworkException("The test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!");
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the test data sheet \"" + datasheetName + "\"!");
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
			throw new FrameworkException("The test case \"" + currentTestcase + "\"" +
										"is not found in the parametrized checkpoints sheet!");
		}
		rowNum = expectedResultsAccess.getRowNum(Integer.toString(currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The iteration number \"" + currentIteration + "\"" +
										"of the test case \"" + currentTestcase + "\"" +
										"is not found in the parametrized checkpoints sheet!");
		}
		
		return expectedResultsAccess.getValue(rowNum, fieldName);
	}
}