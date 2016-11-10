package com.autopia4j.framework.core;

/**
 * Singleton class that encapsulates Framework level global parameters
 * @author vj
 */
public class FrameworkParameters {
	private FrameworkType frameworkType;
	private String basePath;
	private String basePackageName;
	private String runConfiguration;
	private String executionEnvironment;
	private long objectSyncTimeout;
	private long pageLoadTimeout;
	private boolean stopExecution = false;
	private String dateFormatString = "dd-MMM-yyyy hh:mm:ss a";
	
	private static final FrameworkParameters FRAMEWORK_PARAMETERS =
													new FrameworkParameters();
	
	
	private FrameworkParameters() {
		// To prevent external instantiation of this class
	}
	
	/**
	 * Function to return the singleton instance of the {@link FrameworkParameters} object
	 * @return Instance of the {@link FrameworkParameters} object
	 */
	public static FrameworkParameters getInstance() {
		return FRAMEWORK_PARAMETERS;
	}
	
	/**
	 * Function to get the autopia4j {@link FrameworkType}
	 * @return The autopia4j {@link FrameworkType}
	 */
	public FrameworkType getFrameworkType() {
		return frameworkType;
	}
	/**
	 * Function to get the autopia4j {@link FrameworkType}
	 * @param frameworkType The autopia4j {@link FrameworkType}
	 */
	public void setFrameworkType(FrameworkType frameworkType) {
		this.frameworkType = frameworkType;
	}
	
	/**
	 * Function to get the absolute path of the framework (to be used as a relative path)
	 * @return The absolute path of the framework
	 */
	public String getBasePath() {
		return basePath;
	}
	/**
	 * Function to set the absolute path of the framework (to be used as a relative path)
	 * @param basePath The absolute path of the framework
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	/**
	 * Function to get the run configuration to be executed
	 * @return The run configuration
	 */
	public String getRunConfiguration() {
		return runConfiguration;
	}
	/**
	 * Function to set the run configuration to be executed
	 * @param runConfiguration The run configuration
	 */
	public void setRunConfiguration(String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}
	
	/**
	 * Function to get the environment on which the scripts are to be executed
	 * @return The execution environment
	 */
	public String getExecutionEnvironment() {
		return executionEnvironment;
	}
	/**
	 * Function to set the environment on which the scripts are to be executed
	 * @param executionEnvironment The execution environment
	 */
	public void setExecutionEnvironment(String executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}
	
	/**
	 * Function to get the object synchronization timeout
	 * @return The object synchronization timeout
	 */
	public long getObjectSyncTimeout() {
		return objectSyncTimeout;
	}
	/**
	 * Function to set the object synchronization timeout
	 * @param objectSyncTimeout The object synchronization timeout
	 */
	public void setObjectSyncTimeout(long objectSyncTimeout) {
		this.objectSyncTimeout = objectSyncTimeout;
	}
	
	/**
	 * Function to get the page load timeout
	 * @return The page load timeout
	 */
	public long getPageLoadTimeout() {
		return pageLoadTimeout;
	}
	/**
	 * Function to set the page load timeout
	 * @param pageLoadTimeout The page load timeout
	 */
	public void setPageLoadTimeout(long pageLoadTimeout) {
		this.pageLoadTimeout = pageLoadTimeout;
	}
	
	/**
	 * Function to get a boolean value indicating whether to stop the overall test batch execution
	 * @return The stopExecution boolean value
	 */
	public boolean getStopExecution() {
		return stopExecution;
	}
	/**
	 * Function to set a boolean value indicating whether to stop the overall test batch execution
	 * @param stopExecution Boolean value indicating whether to stop the overall test batch execution
	 */
	public void setStopExecution(boolean stopExecution) {
		this.stopExecution = stopExecution;
	}
	
	/**
	 * Function to get the name of the base package containing all the test artifacts
	 * @return The base package name
	 */
	public String getBasePackageName() {
		return basePackageName;
	}
	/**
	 * Function to set the name of the base package containing all the test artifacts
	 * @param basePackageName The base package name
	 */
	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}
	
	/**
	 * Function to get the date format string for the framework
	 * @return The date format string for the framework
	 */
	public String getDateFormatString() {
		return dateFormatString;
	}
	/**
	 * Function to set the date format string for the framework
	 * @param dateFormatString The date format string for the framework
	 */
	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}