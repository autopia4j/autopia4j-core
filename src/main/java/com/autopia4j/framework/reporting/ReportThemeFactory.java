package com.autopia4j.framework.reporting;

import com.autopia4j.framework.utils.FrameworkException;

/**
 * Factory class to create the {@link ReportTheme} object as required
 * @author Cognizant
 */
public class ReportThemeFactory {
	private ReportThemeFactory() {
		// To prevent external instantiation of this class
	}
	
	/**
	 * Enumeration to specify the name of the report theme to be used
	 * @author Cognizant
	 */
	public enum Theme {
		CLASSIC, MYSTIC, AUTUMN, OLIVE, REBEL, RETRO, SERENE
	}
	
	/**
	 * Function to return the {@link ReportTheme} object based on the color {@link Theme} specified
	 * @param theme The color {@link Theme} to be used for the report
	 * @return The {@link ReportTheme} object
	 */
	public static ReportTheme getReportsTheme(Theme theme) {
		ReportTheme reportTheme = new ReportTheme();
		
		switch (theme) {
		case CLASSIC:
			reportTheme.setHeadingBackColor("#495758");
			reportTheme.setHeadingForeColor("#95A3A4");
			reportTheme.setSectionBackColor("#8B9292");
			reportTheme.setSectionForeColor("#001429");
			reportTheme.setContentForeColor("#282A2A");
			reportTheme.setContentBackColor("#EDEEF0");
			break;
			
		case MYSTIC:
			reportTheme.setHeadingBackColor("#4D7C7B");
			reportTheme.setHeadingForeColor("#FFFF95");
			reportTheme.setSectionBackColor("#89B6B5");
			reportTheme.setSectionForeColor("#333300");
			reportTheme.setContentBackColor("#FAFAC5");
			reportTheme.setContentForeColor("#000000");
			break;
			
		case AUTUMN:
			reportTheme.setHeadingBackColor("#9933FF");
			reportTheme.setHeadingForeColor("#FFD1FF");
			reportTheme.setSectionBackColor("#F5E6E6");
			reportTheme.setSectionForeColor("#3D001F");
			reportTheme.setContentForeColor("#8F0047");
			reportTheme.setContentBackColor("#F6F3E4");
			break;
			
		case OLIVE:
			reportTheme.setHeadingBackColor("#86816A");
			reportTheme.setHeadingForeColor("#333300");
			reportTheme.setSectionBackColor("#A6A390");
			reportTheme.setSectionForeColor("#001F00");
			reportTheme.setContentForeColor("#003326");
			reportTheme.setContentBackColor("#E8DEBA");
			break;
			
		case REBEL:
			reportTheme.setHeadingBackColor("#953735");
			reportTheme.setHeadingForeColor("#FFA3A3");
			reportTheme.setSectionBackColor("#CC9999");
			reportTheme.setSectionForeColor("#4D0000");
			reportTheme.setContentForeColor("#3D1F00");
			reportTheme.setContentBackColor("#D9D9D9");
			break;
			
		case RETRO:
			reportTheme.setHeadingBackColor("#CE824E");
			reportTheme.setHeadingForeColor("#291A10");
			reportTheme.setSectionBackColor("#AA9B7C");
			reportTheme.setSectionForeColor("#3D3D29");
			reportTheme.setContentForeColor("#996600");
			reportTheme.setContentBackColor("#F8F1E7");
			break;
			
		case SERENE:
			reportTheme.setHeadingBackColor("#CC8099");
			reportTheme.setHeadingForeColor("#470047");
			reportTheme.setSectionBackColor("#C285C2");
			reportTheme.setSectionForeColor("#3D001F");
			reportTheme.setContentForeColor("#330080");
			reportTheme.setContentBackColor("#C5AFC6");
			break;
			
		default:
			throw new FrameworkException("Invalid report theme!");	
		}
		
		return reportTheme;
	}
}