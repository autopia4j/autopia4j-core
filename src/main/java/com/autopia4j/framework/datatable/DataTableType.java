package com.autopia4j.framework.datatable;

public interface DataTableType {
	
	/**
	 * Function to set the data reference identifier character
	 * @param dataReferenceIdentifier The data reference identifier character
	 */
	void setDataReferenceIdentifier(String dataReferenceIdentifier);
	
	/**
	 * Function to get the Iteration being executed currently
	 * @return The Iteration being executed currently
	 */
	int getCurrentIteration();
	
	/**
	 * Function to set the variables required to uniquely identify the exact row of data under consideration.<br>
	 * Applicable only for modular framework
	 * @param currentTestcase The ID of the current test case
	 * @param currentIteration The Iteration being executed currently
	 */
	void setCurrentRow(String currentTestcase, int currentIteration);
	/**
	 * Function to set the variables required to uniquely identify the exact row of data under consideration.<br>
	 * Applicable only for keyword framework
	 * @param currentTestcase The ID of the current test case
	 * @param currentIteration The Iteration being executed currently
	 * @param currentSubIteration The Sub-Iteration being executed currently
	 */
	void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration);
	
	/**
	 * Function to return the test data value corresponding to the sheet name and field name passed
	 * @param datasheetName The name of the sheet in which the data is present
	 * @param fieldName The name of the field whose value is required
	 * @return The test data present in the field name specified
	 * @see #putData(String, String, String)
	 * @see #getExpectedResult(String)
	 */
	String getData(String datasheetName, String fieldName);
	
	/**
	 * Function to output intermediate data (output values) into the specified sheet
	 * @param datasheetName The name of the sheet into which the data is to be written
	 * @param fieldName The name of the field into which the data is to be written
	 * @param dataValue The value to be written into the field specified
	 * @see #getData(String, String)
	 */
	void putData(String datasheetName, String fieldName, String dataValue);
	
	/**
	 * Function to get the expected result corresponding to the field name passed
	 * @param fieldName The name of the field which contains the expected results
	 * @return The expected result present in the field name specified
	 * @see #getData(String, String)
	 */
	String getExpectedResult(String fieldName);
}