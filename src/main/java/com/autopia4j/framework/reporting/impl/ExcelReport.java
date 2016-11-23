package com.autopia4j.framework.reporting.impl;

import com.autopia4j.framework.core.AutopiaException;
import com.autopia4j.framework.core.TestParameters;
import com.autopia4j.framework.reporting.ReportSettings;
import com.autopia4j.framework.reporting.ReportTheme;
import com.autopia4j.framework.reporting.ReportType;
import com.autopia4j.framework.reporting.Status;
import com.autopia4j.framework.utils.ExcelCellFormatting;
import com.autopia4j.framework.utils.ExcelDataAccess;
import com.autopia4j.framework.utils.Util;

/**
 * Class to encapsulate the Excel report generation functions of the framework
 * @author Cognizant
 */
public class ExcelReport implements ReportType {
	private static final String COVER_PAGE = "Cover_Page";
	private static final String TEST_LOG = "Test_Log";
	private static final String RESULT_SUMMARY = "Result_Summary";
	
	private static final String FONT_NAME = "Verdana";
	
	private static final String STEP_NO = "Step_No";
	private static final String STEP_NAME = "Step_Name";
	private static final String STEP_DESCRIPTION = "Description";
	private static final String STATUS = "Status";
	private static final String STEP_TIME = "Step_Time";
	
	private static final String MODULE = "Module";
	private static final String TEST_CASE = "Test_Case";
	private static final String TEST_INSTANCE = "Test_Instance";
	private static final String TEST_DESCRIPTION = "Test_Description";
	private static final String ADDITIONAL_DETAILS = "Additional_Details";
	private static final String EXECUTION_TIME = "Execution_Time";
	private static final String TEST_STATUS = "Test_Status";
	
	private ExcelDataAccess testLogAccess;
	private ExcelDataAccess resultSummaryAccess;
	
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private ExcelCellFormatting cellFormatting = new ExcelCellFormatting();
	
	private int currentSectionRowNum = 0;
	private int currentSubSectionRowNum = 0;
	
	
	/**
	 * Constructor to initialize the Excel report path and name
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public ExcelReport(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		
		testLogAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										reportSettings.getReportName());
		
		resultSummaryAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										"Summary");
	}
	
	
	/* TEST LOG FUNCTIONS */
	
	@Override
	public void initializeTestLog() {
		testLogAccess.createWorkbook();
		testLogAccess.addSheet(COVER_PAGE);
		testLogAccess.addSheet(TEST_LOG);
		
		initializeTestLogColorPalette();
		
		testLogAccess.setRowSumsBelow(false);
	}
	
	private void initializeTestLogColorPalette() {
		testLogAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		testLogAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		testLogAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
		testLogAccess.setCustomPaletteColor((short) 0x10, "#FF8000");	//Orange (Warning)
		testLogAccess.setCustomPaletteColor((short) 0x11, "#000000");	//Black (Done)
		testLogAccess.setCustomPaletteColor((short) 0x12, "#00FF80");	//Blue (Screenshot)
	}
	
	@Override
	public void addTestLogHeading(String heading) {
		testLogAccess.setDatasheetName(COVER_PAGE);
		int rowNum = testLogAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = testLogAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, heading, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addTestLogSubHeading(String subHeading1, String subHeading2,
										String subHeading3, String subHeading4) {
		testLogAccess.setDatasheetName(COVER_PAGE);
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		testLogAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		testLogAccess.setValue(rowNum, 2, "", cellFormatting);
		testLogAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		testLogAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}
	
	@Override
	public void addTestLogTableHeadings() {
		testLogAccess.setDatasheetName(TEST_LOG);
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.addColumn(STEP_NO, cellFormatting);
		testLogAccess.addColumn(STEP_NAME, cellFormatting);
		testLogAccess.addColumn(STEP_DESCRIPTION, cellFormatting);
		testLogAccess.addColumn(STATUS, cellFormatting);
		testLogAccess.addColumn(STEP_TIME, cellFormatting);
	}
	
	@Override
	public void addTestLogSection(String section) {
		testLogAccess.setDatasheetName(TEST_LOG);
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		currentSectionRowNum = rowNum + 1;
		currentSubSectionRowNum = 0;
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0xA);
		cellFormatting.setForeColorIndex((short) 0xB);
		
		testLogAccess.setValue(rowNum, 0, section, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addTestLogSubSection(String subSection) {
		testLogAccess.setDatasheetName(TEST_LOG);
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);	
		}
		
		currentSubSectionRowNum = rowNum + 1;
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, " " + subSection, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void updateTestLog(String stepNumber, String stepName,
								String stepDescription, Status stepStatus,
								Boolean shouldTakeScreenshot, String screenShotName) {
		testLogAccess.setDatasheetName(TEST_LOG);
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		
		processStatusColumn(stepStatus);
		
		cellFormatting.setCentered(true);
		cellFormatting.setBold(true);
		int columnNum = testLogAccess.getColumnNum(STATUS, 0);
		testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
		
		cellFormatting.setForeColorIndex((short) 0xD);
		cellFormatting.setBold(false);
		testLogAccess.setValue(rowNum, STEP_NO, stepNumber, cellFormatting);
		testLogAccess.setValue(rowNum, STEP_TIME, Util.getCurrentFormattedTime(reportSettings.getDateFormatString()), cellFormatting);
		
		cellFormatting.setCentered(false);
		testLogAccess.setValue(rowNum, STEP_NAME, stepName, cellFormatting);
		
		if (shouldTakeScreenshot) {
			if (reportSettings.shouldLinkScreenshotsToTestLog()) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
				
				testLogAccess.setValue(rowNum, STEP_DESCRIPTION, stepDescription, cellFormatting);
			} else {
				testLogAccess.setValue(rowNum, STEP_DESCRIPTION,
										stepDescription + " (Refer screenshot @ " + screenShotName + ")",
										cellFormatting);
			}
		} else {
			testLogAccess.setValue(rowNum, STEP_DESCRIPTION, stepDescription, cellFormatting);
		}
	}
	
	private void processStatusColumn(Status stepStatus) {
		
		switch (stepStatus) {
		case PASS:
			cellFormatting.setForeColorIndex((short) 0xE);
			break;
			
		case FAIL:
			cellFormatting.setForeColorIndex((short) 0xF);
			break;
			
		case WARNING:
			cellFormatting.setForeColorIndex((short) 0x10);
			break;
			
		case DONE:
			cellFormatting.setForeColorIndex((short) 0x11);
			break;
			
		default:
			throw new AutopiaException("Invalid step status!");
		}
	}
	
	@Override
	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed) {
		testLogAccess.setDatasheetName(TEST_LOG);
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) the previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, "Execution Duration: " + executionTime, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
		
		rowNum = testLogAccess.addRow();
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		testLogAccess.setValue(rowNum, STEP_NO, "Steps passed", cellFormatting);
		testLogAccess.setValue(rowNum, STEP_NAME, ": " + nStepsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		testLogAccess.setValue(rowNum, STEP_DESCRIPTION, "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		testLogAccess.setValue(rowNum, STATUS, "Steps failed", cellFormatting);
		testLogAccess.setValue(rowNum, STEP_TIME, ": " + nStepsFailed, cellFormatting);
		
		wrapUpTestLog();
	}
	
	private void wrapUpTestLog() {
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
		
		testLogAccess.setDatasheetName(COVER_PAGE);
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
	}
	
	
	/* RESULT SUMMARY FUNCTIONS */
	
	@Override
	public void initializeResultSummary() {
		resultSummaryAccess.createWorkbook();
		resultSummaryAccess.addSheet(COVER_PAGE);
		resultSummaryAccess.addSheet(RESULT_SUMMARY);
		
		initializeResultSummaryColorPalette();
	}
	
	private void initializeResultSummaryColorPalette() {
		resultSummaryAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		resultSummaryAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
	}
	
	@Override
	public void addResultSummaryHeading(String heading) {
		resultSummaryAccess.setDatasheetName(COVER_PAGE);
		int rowNum = resultSummaryAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = resultSummaryAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, heading, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	@Override
	public void addResultSummarySubHeading(String subHeading1, String subHeading2,
											String subHeading3, String subHeading4) {
		resultSummaryAccess.setDatasheetName(COVER_PAGE);
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		resultSummaryAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 2, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}
	
	@Override
	public void addResultSummaryTableHeadings() {
		resultSummaryAccess.setDatasheetName(RESULT_SUMMARY);
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.addColumn(MODULE, cellFormatting);
		resultSummaryAccess.addColumn(TEST_CASE, cellFormatting);
		resultSummaryAccess.addColumn(TEST_INSTANCE, cellFormatting);
		resultSummaryAccess.addColumn(TEST_DESCRIPTION, cellFormatting);
		resultSummaryAccess.addColumn(ADDITIONAL_DETAILS, cellFormatting);
		resultSummaryAccess.addColumn(EXECUTION_TIME, cellFormatting);
		resultSummaryAccess.addColumn(TEST_STATUS, cellFormatting);
	}
	
	@Override
	public void updateResultSummary(TestParameters testParameters, String testReportName,
												String executionTime, String testStatus) {
		String moduleName = testParameters.getCurrentModule();
		String testcaseName = testParameters.getCurrentTestcase();
		String testInstanceName = testParameters.getCurrentTestInstance();
		String testcaseDescription = testParameters.getCurrentTestDescription(); 
		String additionalDetails = testParameters.getAdditionalDetails();
		
		resultSummaryAccess.setDatasheetName(RESULT_SUMMARY);
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		cellFormatting.setForeColorIndex((short) 0xD);
		
		cellFormatting.setCentered(false);
		cellFormatting.setBold(false);
		resultSummaryAccess.setValue(rowNum, MODULE, moduleName, cellFormatting);
		resultSummaryAccess.setValue(rowNum, TEST_CASE, testcaseName, cellFormatting);
		
		int columnNum = resultSummaryAccess.getColumnNum(TEST_INSTANCE, 0);
		resultSummaryAccess.setValue(rowNum, columnNum, testInstanceName, cellFormatting);
		if (reportSettings.shouldLinkTestLogsToSummary()) {
			resultSummaryAccess.setHyperlink(rowNum, columnNum, testReportName + ".xls");
		}
		
		resultSummaryAccess.setValue(rowNum, TEST_DESCRIPTION, testcaseDescription, cellFormatting);
		resultSummaryAccess.setValue(rowNum, ADDITIONAL_DETAILS, additionalDetails, cellFormatting);
		
		cellFormatting.setCentered(true);
		resultSummaryAccess.setValue(rowNum, EXECUTION_TIME, executionTime, cellFormatting);
		
		cellFormatting.setBold(true);
		if ("Passed".equalsIgnoreCase(testStatus)) {
			cellFormatting.setForeColorIndex((short) 0xE);
		}
		if ("Failed".equalsIgnoreCase(testStatus)) {
			cellFormatting.setForeColorIndex((short) 0xF);
		}
		resultSummaryAccess.setValue(rowNum, TEST_STATUS, testStatus, cellFormatting);
	}
	
	@Override
	public void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed) {	
		resultSummaryAccess.setDatasheetName(RESULT_SUMMARY);
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName(FONT_NAME);
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBold(true);
		cellFormatting.setCentered(true);
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, "Total Duration: " +
													totalExecutionTime, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 6);
		
		rowNum = resultSummaryAccess.addRow();
		cellFormatting.setCentered(false);
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		resultSummaryAccess.setValue(rowNum, MODULE, "Tests passed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, TEST_CASE, ": " + nTestsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		resultSummaryAccess.setValue(rowNum, TEST_INSTANCE, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, TEST_DESCRIPTION, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, ADDITIONAL_DETAILS, "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		resultSummaryAccess.setValue(rowNum, EXECUTION_TIME, "Tests failed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, TEST_STATUS, ": " + nTestsFailed, cellFormatting);
		
		wrapUpResultSummary();
	}
	
	private void wrapUpResultSummary() {
		resultSummaryAccess.autoFitContents(0, 6);
		resultSummaryAccess.addOuterBorder(0, 6);
		
		resultSummaryAccess.setDatasheetName(COVER_PAGE);
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
	}
}