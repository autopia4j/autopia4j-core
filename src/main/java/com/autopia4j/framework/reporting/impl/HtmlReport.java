package com.autopia4j.framework.reporting.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.autopia4j.framework.core.TestParameters;
import com.autopia4j.framework.reporting.ReportSettings;
import com.autopia4j.framework.reporting.ReportTheme;
import com.autopia4j.framework.reporting.ReportType;
import com.autopia4j.framework.reporting.Status;
import com.autopia4j.framework.utils.FrameworkException;
import com.autopia4j.framework.utils.Util;


/**
 * Class to encapsulate the HTML report generation functions of the framework
 * @author Cognizant
 */
public class HtmlReport implements ReportType {
	private String testLogPath, resultSummaryPath;
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	
	private boolean isTestLogHeaderTableCreated = false;
	private boolean isTestLogMainTableCreated = false;
	private boolean isResultSummaryHeaderTableCreated = false;
	private boolean isResultSummaryMainTableCreated = false;
	
	private String currentSection = "";
	private String currentSubSection = "";
	private int currentContentNumber = 1;
	
	
	/**
	 * Constructor to initialize the HTML report
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public HtmlReport(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		
		testLogPath = reportSettings.getReportPath() + Util.getFileSeparator() + "HTML Results"
							+ Util.getFileSeparator() + reportSettings.getReportName() + ".html";
		
		resultSummaryPath = reportSettings.getReportPath() + Util.getFileSeparator() +
								"HTML Results" + Util.getFileSeparator() + "Summary" + ".html";
	}
	
	private String getThemeCss() {
		return  "\t\t <style type='text/css'> \n" +
					"\t\t\t body { \n" +
						"\t\t\t\t background-color: " + reportTheme.getContentForeColor() +"; \n" +
						"\t\t\t\t font-family: Verdana, Geneva, sans-serif; \n" +
						"\t\t\t\t text-align: center; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t small { \n" +
						"\t\t\t\t font-size: 0.7em; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t table { \n" +
						"\t\t\t\t border: 1px solid #4D7C7B; \n" +
						"\t\t\t\t border-collapse: collapse; \n" +
						"\t\t\t\t border-spacing: 0px; \n" +
						"\t\t\t\t width: 95%; \n" +
						"\t\t\t\t margin-left: auto; \n" +
						"\t\t\t\t margin-right: auto; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.heading { \n" +
						"\t\t\t\t background-color: " + reportTheme.getHeadingBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getHeadingForeColor() + "; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t font-weight: bold; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.subheading { \n" +
						"\t\t\t\t background-color: " + reportTheme.getHeadingForeColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getHeadingBackColor() + "; \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.section { \n" +
						"\t\t\t\t background-color: " + reportTheme.getSectionBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getSectionForeColor() + "; \n" +
						"\t\t\t\t cursor: pointer; \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.subsection { \n" +
						"\t\t\t\t cursor: pointer; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.content { \n" +
						"\t\t\t\t background-color: " + reportTheme.getContentBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getContentForeColor() + "; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t display: table-row; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td { \n" +
						"\t\t\t\t padding: 4px; \n" +
						"\t\t\t\t text-align: inherit\\0/; \n" +
						"\t\t\t\t word-wrap: break-word; \n" +
						"\t\t\t\t max-width: 450px; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t th { \n" +
					"\t\t\t\t padding: 4px; \n" +
					"\t\t\t\t text-align: inherit\\0/; \n" +
					"\t\t\t\t word-break: break-all; \n" +
					"\t\t\t\t max-width: 450px; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.justified { \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.pass { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: green; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.fail { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: red; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.done, td.screenshot { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: black; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.debug { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: blue; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.warning { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: orange; \n" +
					"\t\t\t } \n" +
				 "\t\t </style> \n\n";
	}
	
	private String getJavascriptFunctions() {
		return	"\t\t <script> \n" +
					"\t\t\t function toggleMenu(objID) { \n" +
						"\t\t\t\t if (!document.getElementById) return; \n" +
						"\t\t\t\t var ob = document.getElementById(objID).style; \n" +
						"\t\t\t\t if(ob.display === 'none') { \n" +
							"\t\t\t\t\t try { \n" +
								"\t\t\t\t\t\t ob.display='table-row-group'; \n" +
							"\t\t\t\t\t } catch(ex) { \n" +
								"\t\t\t\t\t\t ob.display='block'; \n" +
							"\t\t\t\t\t } \n" +
						"\t\t\t\t } \n" +
						"\t\t\t\t else { \n" +
							"\t\t\t\t\t ob.display='none'; \n" +
						"\t\t\t\t } \n" +
					"\t\t\t } \n" +
					
					"\t\t\t function toggleSubMenu(objId) { \n" +
						"\t\t\t\t for(i=1; i<10000; i++) { \n" +
							"\t\t\t\t\t var ob = document.getElementById(objId.concat(i)); \n" +
							"\t\t\t\t\t if(ob === null) { \n" +
								"\t\t\t\t\t\t break; \n" +
							"\t\t\t\t\t } \n" +
							"\t\t\t\t\t if(ob.style.display === 'none') { \n" +
								"\t\t\t\t\t\t try { \n" +
									"\t\t\t\t\t\t\t ob.style.display='table-row'; \n" +
								"\t\t\t\t\t\t } catch(ex) { \n" +
									"\t\t\t\t\t\t\t ob.style.display='block'; \n" +
								"\t\t\t\t\t\t } \n" +
							"\t\t\t\t\t } \n" +
							"\t\t\t\t\t else { \n" +
								"\t\t\t\t\t\t ob.style.display='none'; \n" +
							"\t\t\t\t\t } \n" +
						"\t\t\t\t } \n" +
					"\t\t\t } \n" +
				 "\t\t </script> \n";
	}
	
	
	/* TEST LOG FUNCTIONS*/
	
	@Override
	public void initializeTestLog() {
		File testLogFile = new File(testLogPath);
		try {
			testLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating HTML test log file");
		}
		
		PrintStream printStream;
		try {
			printStream = new PrintStream(testLogFile, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing into HTML test log file");
		}
		
		String testLogHeadSection;
		testLogHeadSection = 	"<!DOCTYPE html> \n" +
								"<html> \n" +
								"\t <head> \n" +
									"\t\t <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'> \n" +
									"\t\t <title>" +
										reportSettings.getProjectName() +
										" - " +	reportSettings.getReportName() +
										" Automation Execution Results" +
									"</title> \n\n" +
									getThemeCss() +
									getJavascriptFunctions() +
								"\t </head> \n";
		
        printStream.println(testLogHeadSection);
        printStream.close();
	}
	
	@Override
	public void addTestLogHeading(String heading) {
		if(!isTestLogHeaderTableCreated) {
			createTestLogHeaderTable();
			isTestLogHeaderTableCreated = true;
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogHeading =	"\t\t\t\t <tr class='heading'> \n" +
										"\t\t\t\t\t <th colspan='4' style='font-family:Copperplate Gothic; font-size:1.4em;'> \n" + 
											"\t\t\t\t\t\t " + heading + " \n" +
										"\t\t\t\t\t </th> \n" +
									"\t\t\t\t </tr> \n";
		    bufferedWriter.append(testLogHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding heading to HTML test log");
		}
	}
	
	private void createTestLogHeaderTable() {
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogHeaderTable =	"\t <body> \n" +
											"\t\t <table id='header'> \n" +
												"\t\t\t <thead> \n";
		    bufferedWriter.append(testLogHeaderTable);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding header table to HTML test log");
		}
	}
	
	@Override
	public void addTestLogSubHeading(String subHeading1, String subHeading2,
										String subHeading3, String subHeading4) {
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogSubHeading =	"\t\t\t\t <tr class='subheading'> \n" +
											"\t\t\t\t\t <th>&nbsp;" + subHeading1.replace(" ", "&nbsp;") + "</th> \n" +
											"\t\t\t\t\t <th>&nbsp;" + subHeading2.replace(" ", "&nbsp;") + "</th> \n" +
											"\t\t\t\t\t <th>&nbsp;" + subHeading3.replace(" ", "&nbsp;") + "</th> \n" +
											"\t\t\t\t\t <th>&nbsp;" + subHeading4.replace(" ", "&nbsp;") + "</th> \n" +
										"\t\t\t\t </tr> \n";
		    bufferedWriter.append(testLogSubHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding sub-heading to HTML test log");
		}
	}
	
	private void createTestLogMainTable() {
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogMainTable =		"\t\t\t </thead> \n" +
										 "\t\t </table> \n\n" +
										 
										 "\t\t <table id='main'> \n";
			
		    bufferedWriter.append(testLogMainTable);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table to HTML test log");
		}
	}
	
	@Override
	public void addTestLogTableHeadings() {
		if(!isTestLogMainTableCreated) {
			createTestLogMainTable();
			isTestLogMainTableCreated = true;
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogTableHeading =	"\t\t\t <thead> \n" +
												"\t\t\t\t <tr class='heading'> \n" + 
													"\t\t\t\t\t <th>Step No</th> \n" +
													"\t\t\t\t\t <th>Step Name</th> \n" +
													"\t\t\t\t\t <th>Description</th> \n" +
													"\t\t\t\t\t <th>Status</th> \n" +
													"\t\t\t\t\t <th>Step Time</th> \n" +
												"\t\t\t\t </tr> \n" +
											"\t\t\t </thead> \n\n";
		    bufferedWriter.append(testLogTableHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table headings to HTML test log");
		}
	}
	
	@Override
	public void addTestLogSection(String section) {
		String testLogSection = "";
		if (!"".equals(currentSection)) {
			testLogSection = "\t\t\t </tbody>";
		}
		
		currentSection = section.replaceAll("[^a-zA-Z0-9]", "");
		
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    testLogSection +=	"\t\t\t <tbody> \n" +
										"\t\t\t\t <tr class='section'> \n" +
											"\t\t\t\t\t <td colspan='5' onclick=\"toggleMenu('" + currentSection + "')\">+ " +
												section + "</td> \n" +
										"\t\t\t\t </tr> \n" +
									"\t\t\t </tbody> \n" +
									"\t\t\t <tbody id='" + currentSection + "' style='display:table-row-group'> \n";
		    bufferedWriter.append(testLogSection);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding section to HTML test log");
		}
	}
	
	@Override
	public void addTestLogSubSection(String subSection) {
		currentSubSection = subSection.replaceAll("[^a-zA-Z0-9]", "");
		currentContentNumber = 1;
		
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String testLogSubSection =	"\t\t\t\t <tr class='subheading subsection'> \n" +
											"\t\t\t\t\t <td colspan='5' onclick=\"toggleSubMenu('" + currentSection + currentSubSection + "')\">&nbsp;+ " +
												subSection + "</td> \n" +
										"\t\t\t\t </tr> \n";
		    bufferedWriter.append(testLogSubSection);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding sub-section to HTML test log");
		}
	}
	
	@Override
	public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus,
																Boolean shouldTakeScreenshot, String screenShotName) {
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
			String testStepRow = "\t\t\t\t <tr class='content' id='" + currentSection + currentSubSection + currentContentNumber + "'> \n" +
									"\t\t\t\t\t <td>" + stepNumber + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + stepName + "</td> \n";
			currentContentNumber++;
			
			if(shouldTakeScreenshot) {
				testStepRow += getTestStepWithScreenshot(stepDescription, stepStatus, screenShotName);
			} else {
				testStepRow += getTestStepWithoutScreenshot(stepDescription, stepStatus);
			}
	       	testStepRow +=	 "\t\t\t\t\t <td>" +
       										"<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>" +
       									"</td> \n" +
							"\t\t\t\t </tr> \n";
	       	
		    bufferedWriter.append(testStepRow);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML test log");
		}
	}
	
	private String getTestStepWithScreenshot(String stepDescription, Status stepStatus, String screenShotName) {
		String testStepRow;
		
		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
									stepDescription +
								"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
							 		"<a href='..\\Screenshots\\" + screenShotName + "'>" +
				 						stepStatus +
				 					"</a>" +
				 				"</td> \n";
		} else {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
									stepDescription + " (Refer Screenshot @ " + screenShotName + ")" +
								"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
				 					stepStatus +
				 				"</td> \n";
		}
		
		return testStepRow;
	}
	
	private String getTestStepWithoutScreenshot(String stepDescription, Status stepStatus) {
		String testStepRow;
		
		testStepRow = 
				"\t\t\t\t\t <td class='justified'>" +
								stepDescription +
							"</td> \n" +
				"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
			 					stepStatus +
			 				"</td> \n";
		
		return testStepRow;
	}
	
	@Override
	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed) {
		try {
			FileOutputStream outputStream = new FileOutputStream(testLogPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		    
			String testLogFooter =	"\t\t\t </tbody> \n" +
								"\t\t </table> \n\n" +
								
								"\t\t <table id='footer'> \n" +
									"\t\t\t <colgroup> \n" +
										"\t\t\t\t <col style='width: 25%' /> \n" +
										"\t\t\t\t <col style='width: 25%' /> \n" +
										"\t\t\t\t <col style='width: 25%' /> \n" +
										"\t\t\t\t <col style='width: 25%' /> \n" +
									"\t\t\t </colgroup> \n\n" +
									
									"\t\t\t <tfoot> \n" +
										"\t\t\t\t <tr class='heading'> \n" + 
											"\t\t\t\t\t <th colspan='4'>Execution Duration: " + executionTime + "</th> \n" + 
										"\t\t\t\t </tr> \n" +
										"\t\t\t\t <tr class='subheading'> \n" + 
											"\t\t\t\t\t <td class='pass'>&nbsp;Steps passed</td> \n" + 
											"\t\t\t\t\t <td class='pass'>&nbsp;: " + nStepsPassed + "</td> \n" +
											"\t\t\t\t\t <td class='fail'>&nbsp;Steps failed</td> \n" + 
											"\t\t\t\t\t <td class='fail'>&nbsp;: " + nStepsFailed + "</td> \n" +
										"\t\t\t\t </tr> \n" +
									"\t\t\t </tfoot> \n" +
								"\t\t </table> \n" +
							"\t </body> \n" +
						"</html>";
		    
		    bufferedWriter.append(testLogFooter);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding footer to HTML test log");
		}
	}
	
	
	/* RESULT SUMMARY FUNCTIONS*/
	
	@Override
	public void initializeResultSummary() {
		File resultSummaryFile = new File(resultSummaryPath);
		
		try {
			resultSummaryFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while creating HTML result summary file");
		}
		
		PrintStream printStream;
		try {
			printStream = new PrintStream(resultSummaryFile, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing into HTML result summary file");
		}
		
		String resultSummaryHeader;
		resultSummaryHeader = 	"<!DOCTYPE html> \n" +
								"<html> \n" +
								"\t <head> \n" +
									"\t\t <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'> \n" +
									"\t\t <title>" +
										reportSettings.getProjectName() +
										" - Automation Execution Results Summary" +
									"</title> \n\n" +
									getThemeCss() +
									getJavascriptFunctions() +
								"\t </head> \n";
		
		printStream.println (resultSummaryHeader);
        printStream.close();
	}
	
	@Override
	public void addResultSummaryHeading(String heading) {
		if(!isResultSummaryHeaderTableCreated) {
			createResultSummaryHeaderTable();
			isResultSummaryHeaderTableCreated = true;
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummaryHeading =	"\t\t\t\t <tr class='heading'> \n" +
												"\t\t\t\t\t <th colspan='4' style='font-family:Copperplate Gothic; font-size:1.4em;'> \n" + 
													"\t\t\t\t\t\t " + heading + " \n" +
												"\t\t\t\t\t </th> \n" +
											"\t\t\t\t </tr> \n";
		    bufferedWriter.append(resultSummaryHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding heading to HTML result summary");
		}
	}
	
	private void createResultSummaryHeaderTable() {
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummaryHeaderTable =	"\t <body> \n" +
													"\t\t <table id='header'> \n" +
														"\t\t\t <thead> \n";
		    bufferedWriter.append(resultSummaryHeaderTable);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding header table to HTML result summary");
		}
	}
	
	@Override
	public void addResultSummarySubHeading(String subHeading1, String subHeading2,
											String subHeading3, String subHeading4) {
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummarySubHeading =	"\t\t\t\t <tr class='subheading'> \n" +
													"\t\t\t\t\t <th>&nbsp;" + subHeading1.replace(" ", "&nbsp;") + "</th> \n" +
													"\t\t\t\t\t <th>&nbsp;" + subHeading2.replace(" ", "&nbsp;") + "</th> \n" +
													"\t\t\t\t\t <th>&nbsp;" + subHeading3.replace(" ", "&nbsp;") + "</th> \n" +
													"\t\t\t\t\t <th>&nbsp;" + subHeading4.replace(" ", "&nbsp;") + "</th> \n" +
												"\t\t\t\t </tr> \n";
		    bufferedWriter.append(resultSummarySubHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding sub-heading to HTML result summary");
		}
	}
	
	private void createResultSummaryMainTable() {
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummaryMainTable =	"\t\t\t </thead> \n" +
										 "\t\t </table> \n\n" +
										 
										 "\t\t <table id='main'> \n" + 
											"\t\t\t <colgroup> \n";
		    
		    bufferedWriter.append(resultSummaryMainTable);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table to HTML result summary");
		}
	}
	
	@Override
	public void addResultSummaryTableHeadings() {
		if(!isResultSummaryMainTableCreated) {
			createResultSummaryMainTable();
			isResultSummaryMainTableCreated = true;
		}
		
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummaryTableHeading =	"\t\t\t <thead> \n" +
												"\t\t\t\t <tr class='heading'> \n" + 
													"\t\t\t\t\t <th>Module</th> \n" +
													"\t\t\t\t\t <th>Test Case</th> \n" +
													"\t\t\t\t\t <th>Test Instance</th> \n" +
													"\t\t\t\t\t <th>Test Description</th> \n" +
													"\t\t\t\t\t <th>Additional Details</th> \n" +
													"\t\t\t\t\t <th>Execution Time</th> \n" +
													"\t\t\t\t\t <th>Test Status</th> \n" +
												"\t\t\t\t </tr> \n" +
											"\t\t\t </thead> \n\n";
		    bufferedWriter.append(resultSummaryTableHeading);
		    bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table headings to HTML result summary");
		}
	}
	
	@Override
	public void updateResultSummary(TestParameters testParameters, String testReportName,
												String executionTime, String testStatus) {
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		    
			String testcaseRow;
			String moduleName = testParameters.getCurrentModule();
			String testcaseName = testParameters.getCurrentTestcase();
			String testInstanceName = testParameters.getCurrentTestInstance();
			String testcaseDescription = testParameters.getCurrentTestDescription();
			String additionalDetails = testParameters.getAdditionalDetails();
			
			if (reportSettings.shouldLinkTestLogsToSummary()) {
				testcaseRow = 	"\t\t\t\t <tr class='content' > \n" +
									"\t\t\t\t\t <td class='justified'>" + moduleName + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + testcaseName + "</td> \n" +
									"\t\t\t\t\t <td class='justified'><a href='" + testReportName + ".html' " +
														"target='about_blank'>" + testInstanceName + "</a>" +
												"</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + testcaseDescription + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + additionalDetails + "</td> \n" +
									"\t\t\t\t\t <td>" + executionTime + "</td> \n";
			} else {
				testcaseRow = 	"\t\t\t\t <tr class='content' > \n" +
									"\t\t\t\t\t <td class='justified'>" + moduleName + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + testcaseName + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + testInstanceName + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + testcaseDescription + "</td> \n" +
									"\t\t\t\t\t <td class='justified'>" + additionalDetails + "</td> \n" +
									"\t\t\t\t\t <td>" + executionTime + "</td> \n";
			}
		    
			if("passed".equalsIgnoreCase(testStatus)) {
				testcaseRow += 		"\t\t\t\t\t <td class='pass'>" + testStatus + "</td> \n" +
								"\t\t\t\t </tr> \n";
			} else {
				testcaseRow += 		"\t\t\t\t\t <td class='fail'>" + testStatus + "</td> \n" +
								"\t\t\t\t </tr> \n";
			}
			
		    bufferedWriter.append(testcaseRow);
		   	bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML result summary");
		}
	}
	
	@Override
	public void addResultSummaryFooter(String totalExecutionTime,
										int nTestsPassed, int nTestsFailed) {
		try {
			FileOutputStream outputStream = new FileOutputStream(resultSummaryPath, true);
			OutputStreamWriter outputStreamWriter =
					new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			
		    String resultSummaryFooter =	"\t\t\t </tbody> \n" +
										"\t\t </table> \n\n" +
										
										"\t\t <table id='footer'> \n" +
											"\t\t\t <colgroup> \n" +
												"\t\t\t\t <col style='width: 25%' /> \n" +
												"\t\t\t\t <col style='width: 25%' /> \n" +
												"\t\t\t\t <col style='width: 25%' /> \n" +
												"\t\t\t\t <col style='width: 25%' /> \n" +
											"\t\t\t </colgroup> \n\n" +
											
											"\t\t\t <tfoot> \n" +
												"\t\t\t\t <tr class='heading'> \n" + 
													"\t\t\t\t\t <th colspan='4'>Total Duration: " + totalExecutionTime + "</th> \n" + 
												"\t\t\t\t </tr> \n" +
												"\t\t\t\t <tr class='subheading'> \n" + 
													"\t\t\t\t\t <td class='pass'>&nbsp;Tests passed</td> \n" + 
													"\t\t\t\t\t <td class='pass'>&nbsp;: " + nTestsPassed + "</td> \n" +
													"\t\t\t\t\t <td class='fail'>&nbsp;Tests failed</td> \n" + 
													"\t\t\t\t\t <td class='fail'>&nbsp;: " + nTestsFailed + "</td> \n" +
												"\t\t\t\t </tr> \n" +
											"\t\t\t </tfoot> \n" +
										"\t\t </table> \n" +
									"\t </body> \n" +
								"</html>";
		    
		    bufferedWriter.append(resultSummaryFooter);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding footer to HTML result summary");
		}
	}
}