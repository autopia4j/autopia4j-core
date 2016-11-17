package com.autopia4j.framework.datatable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.utils.ExcelDataAccess;
import com.autopia4j.framework.utils.Util;

/**
 * Abstract base class for datatables within the autopia4j framework
 * @author vj
 */
public abstract class DataTable {
	private final Logger logger = LoggerFactory.getLogger(DataTable.class);
	protected final String datatablePath;
	protected final String datatableName;
	protected String dataReferenceIdentifier = "#";
	
	protected String currentTestcase;
	
	
	/**
	 * Constructor to initialize the {@link DataTable} object
	 * @param datatablePath The path where the datatable is stored
	 * @param datatableName The name of the datatable file
	 */
	public DataTable(String datatablePath, String datatableName) {
		this.datatablePath = datatablePath;
		this.datatableName = datatableName;
		
		logger.info("Initializing datatable @ " + datatablePath +
						Util.getFileSeparator() + datatableName + ".xls");
	}
	
	/**
	 * Function to get the path where the datatable is stored
	 * @return The path where the datatable is stored
	 */
	public String getDatatablePath() {
		return datatablePath;
	}
	
	/**
	 * Function to get the name of the datatable file
	 * @return The name of the datatable file
	 */
	public String getDatatableName() {
		return datatableName;
	}
	
	/**
	 * Function to set the data reference identifier character
	 * @param dataReferenceIdentifier The data reference identifier character
	 */
	public void setDataReferenceIdentifier(String dataReferenceIdentifier) {
		if (dataReferenceIdentifier.length() != 1) {
			String errorMessage = "The data reference identifier must be a single character!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		this.dataReferenceIdentifier = dataReferenceIdentifier;
	}
	
	/**
	 * Function to set the variables required to uniquely identify the exact row of data under consideration.<br>
	 * Applicable only for modular framework
	 * @param currentTestcase The ID of the current test case
	 */
	public abstract void setCurrentRow(String currentTestcase);
	/**
	 * Function to set the variables required to uniquely identify the exact row of data under consideration.<br>
	 * Applicable only for modular framework
	 * @param currentTestcase The ID of the current test case
	 * @param currentIteration The Iteration being executed currently
	 */
	public abstract void setCurrentRow(String currentTestcase, int currentIteration);
	/**
	 * Function to set the variables required to uniquely identify the exact row of data under consideration.<br>
	 * Applicable only for keyword framework
	 * @param currentTestcase The ID of the current test case
	 * @param currentIteration The Iteration being executed currently
	 * @param currentSubIteration The Sub-Iteration being executed currently
	 */
	public abstract void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration);
	
	/**
	 * Function to return the test data value corresponding to the sheet name and field name passed
	 * @param datasheetName The name of the sheet in which the data is present
	 * @param fieldName The name of the field whose value is required
	 * @return The test data present in the field name specified
	 * @see #putData(String, String, String)
	 * @see #getExpectedResult(String)
	 */
	public abstract String getData(String datasheetName, String fieldName);
	
	protected String getCommonData(String fieldName, String dataValue) {
		ExcelDataAccess commonDataAccess = new ExcelDataAccess(datatablePath, "Common Testdata");
		commonDataAccess.setDatasheetName("Common_Testdata");
		
		String dataReferenceId = dataValue.split(dataReferenceIdentifier)[1];
		
		int rowNum = commonDataAccess.getRowNum(dataReferenceId, 0, 1);	// Start at row 1, skipping the header row
		if (rowNum == -1) {
			String errorMessage = "The common test data row identified by \"" + dataReferenceId + "\"" +
										"is not found in the common test data sheet!";
			logger.error(errorMessage);
			throw new AutopiaException(errorMessage);
		}
		
		return commonDataAccess.getValue(rowNum, fieldName);
	}
	
	/**
	 * Function to output intermediate data (output values) into the specified sheet
	 * @param datasheetName The name of the sheet into which the data is to be written
	 * @param fieldName The name of the field into which the data is to be written
	 * @param dataValue The value to be written into the field specified
	 * @see #getData(String, String)
	 */
	public abstract void putData(String datasheetName, String fieldName, String dataValue);
	
	/**
	 * Function to get the expected result corresponding to the field name passed
	 * @param fieldName The name of the field which contains the expected results
	 * @return The expected result present in the field name specified
	 * @see #getData(String, String)
	 */
	public abstract String getExpectedResult(String fieldName);
}