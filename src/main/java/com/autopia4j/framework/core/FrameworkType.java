package com.autopia4j.framework.core;

/**
 * Enum to represent the type of automation framework being used
 * @author vj
 */
public enum FrameworkType {
	/**
	 * Keyword-driven framework with no datatable
	 */
	KEYWORD_BASIC,
	/**
	 * Keyword-driven framework with a non-iterative datatable
	 */
	KEYWORD_NONITERATIVE,
	/**
	 * Keyword-driven framework with a sub-iterative datatable
	 */
	KEYWORD_SUBITERATIVE,
	
	/**
	 * Modular framework with no datatable
	 */
	MODULAR_BASIC,
	/**
	 * Modular framework with a non-iterative datatable
	 */
	MODULAR_NONITERATIVE,
	/**
	 * Modular framework with an iterative datatable
	 */
	MODULAR_ITERATIVE,
	
	/**
	 * Cucumber-JVM based framework
	 */
	CUCUMBER;
}
