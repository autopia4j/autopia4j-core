package com.autopia4j.framework.reporting;

import java.io.File;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;


/**
 * Class to encapsulate the reporting settings in the framework
 * @author Cognizant
 */
public class ReportSettings {
	private final Logger logger = LoggerFactory.getLogger(ReportSettings.class);
	private final String reportPath;
	private final String reportName;
	
	private String projectName;
	private int logLevel;
	private String dateFormatString;
	
	private boolean generateExcelReports;
	private boolean generateHtmlReports;
	
	private boolean linkScreenshotsToTestLog;
	private boolean linkTestLogsToSummary;
	
	private boolean consolidateScreenshotsInWordDoc;
	
	
	/**
	 * Constructor to initialize the report settings
	 * @param reportPath The report path
	 * @param reportName The report name
	 */
	public ReportSettings(String reportPath, String reportName) {
		logger.info("Initializing report {} @ {}", reportName, reportPath);
		boolean reportPathExists = new File(reportPath).isDirectory();
		if(!reportPathExists) {
			String errorDescription = "The given report path does not exist!";
			logger.error(errorDescription);
			throw new AutopiaException(errorDescription);
		}
		this.reportPath = reportPath;
		this.reportName = reportName;
		
		// Set default values for all the report settings
		projectName = "";
		logLevel = 3;
		generateExcelReports = true;
		generateHtmlReports = true;
		linkScreenshotsToTestLog = true;
		linkTestLogsToSummary = true;
		consolidateScreenshotsInWordDoc = false;
		dateFormatString = "dd-MMM-yyyy hh:mm:ss a";
	}
	
	/**
	 * Function to get the absolute path where the report is to be stored
	 * @return The report path
	 */
	public String getReportPath() {
		return reportPath;
	}
	
	/**
	 * Function to get the name of the report
	 * @return The report name
	 */
	public String getReportName() {
		return reportName;
	}
	
	/**
	 * Function to get the name of the project being automated
	 * @return The project name
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Function to set the name of the project being automated
	 * @param projectName The project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * Function to get the logging level of the reports.
	 * Log levels range between 0 to 4,
	 * with 0 being minimal reporting and 4 being highly detailed reporting
	 * @return The log level
	 */
	public int getLogLevel() {
		return logLevel;
	}
	
	/**
	 * Function to set the logging level of the reports.
	 * Log levels range between 0 to 4,
	 * with 0 being minimal reporting and 4 being highly detailed reporting
	 * @param logLevel The log level
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
		
		if (logLevel < 0) {
			this.logLevel = 0;
		}
		
		if (logLevel > 4) {
			this.logLevel = 4;
		}
	}
	
	/**
	 * Function to get a string indicating the format for the date/time to be used within the report
	 * @return The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public String getDateFormatString() {
		return dateFormatString;
	}
	
	/**
	 * Function to set a string indicating the format for the date/time to be used within the report
	 * @param dateFormatString The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}
	
	/**
	 * Function to get a Boolean value indicating whether Excel reports should be generated
	 * @return Boolean value indicating whether Excel reports should be generated
	 */
	public boolean shouldGenerateExcelReports() {
		return generateExcelReports;
	}
	
	/**
	 * Function to set a Boolean value indicating whether Excel reports should be generated
	 * @param generateExcelReports Boolean value indicating whether Excel reports should be generated
	 */
	public void setGenerateExcelReports(boolean generateExcelReports) {
		this.generateExcelReports = generateExcelReports;
	}
	
	/**
	 * Function to get a Boolean value indicating whether HTML reports should be generated
	 * @return Boolean value indicating whether HTML reports should be generated
	 */
	public boolean shouldGenerateHtmlReports() {
		return generateHtmlReports;
	}
	
	/**
	 * Function to set a Boolean value indicating whether HTML reports should be generated
	 * @param generateHtmlReports Boolean value indicating whether HTML reports should be generated
	 */
	public void setGenerateHtmlReports(boolean generateHtmlReports) {
		this.generateHtmlReports = generateHtmlReports;
	}
	
	/**
	 * Function to get a Boolean value indicating whether any screenshot taken must be linked to the corresponding step within the test log
	 * @return Boolean value indicating whether any screenshot taken must be linked to the corresponding step within the test log
	 */
	public boolean shouldLinkScreenshotsToTestLog() {
		return linkScreenshotsToTestLog;
	}
	
	/**
	 * Function to set a Boolean value indicating whether any screenshot taken must be linked to the corresponding step within the test log
	 * @param linkScreenshotsToTestLog Boolean value indicating whether any screenshot taken must be linked to the corresponding step within the test log
	 */
	public void setLinkScreenshotsToTestLog(boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}
	
	/**
	 * Function to get a Boolean value indicating whether the individual test logs must be linked to the result summary
	 * @return Boolean value indicating whether the individual test logs must be linked to the result summary
	 */
	public boolean shouldLinkTestLogsToSummary() {
		return linkTestLogsToSummary;
	}
	
	/**
	 * Function to set a Boolean value indicating whether the individual test logs must be linked to the result summary
	 * @param linkTestLogsToSummary Boolean value indicating whether the individual test logs must be linked to the result summary
	 */
	public void setLinkTestLogsToSummary(boolean linkTestLogsToSummary) {
		this.linkTestLogsToSummary = linkTestLogsToSummary;
	}
	
	/**
	 * Function to get a Boolean value indicating whether all the screenshots must be consolidated into a Word document
	 * @return Boolean value indicating whether all the screenshots must be consolidated into a Word document
	 */
	public boolean shouldConsolidateScreenshotsInWordDoc() {
		return consolidateScreenshotsInWordDoc;
	}
	
	/**
	 * Function to set a Boolean value indicating whether all the screenshots must be consolidated into a Word document
	 * @param consolidateScreenshotsInWordDoc Boolean value indicating whether all the screenshots must be consolidated into a Word document
	 */
	public void setConsolidateScreenshotsInWordDoc(
			boolean consolidateScreenshotsInWordDoc) {
		this.consolidateScreenshotsInWordDoc = consolidateScreenshotsInWordDoc;
	}
}