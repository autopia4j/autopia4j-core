package com.autopia4j.framework.utils;

/**
 * Class to encapsulate the cell formatting settings for the Excel spreadsheet
 * @author vj
 */
public class ExcelCellFormatting {
	private String fontName;
	private short fontSize;
	private short backColorIndex;
	private short foreColorIndex;
    private boolean bold = false;
    private boolean italics = false;
    private boolean centered = false;
	
	
	/**
	 * Function to get the name of the font to be used in the cell
	 * @return The font name
	 */
    public String getFontName() {
		return fontName;
	}
	
	/**
	 * Function to set the name of the font to be used in the cell
	 * @param fontName The font name
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	/**
	 * Function to get the font size to be used in the cell
	 * @return The font size
	 */
	public short getFontSize() {
		return fontSize;
	}
	
	/**
	 * Function to set the font size to be used in the cell
	 * @param fontSize The font size
	 */
	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}
	
	/**
	 * Function to get the index of the background color for the cell
	 * @return The background color index
	 */
	public short getBackColorIndex() {
    	return backColorIndex;
    }
    
    /**
	 * Function to set the index of the background color for the cell
	 * @param backColorIndex The background color index
	 */
	public void setBackColorIndex(short backColorIndex) {
    	if(backColorIndex < 0x8 || backColorIndex > 0x40) {
			throw new FrameworkException("Valid indexes for the Excel custom palette are from 0x8 to 0x40 (inclusive)!");
		}
    	
    	this.backColorIndex = backColorIndex;
    }
    
    /**
	 * Function to get the index of the foreground color (i.e., font color) for the cell
	 * @return The foreground color (font color) index
	 */
	public short getForeColorIndex() {
    	return foreColorIndex;
    }
    
    /**
     * Function to set the index of the foreground color (i.e., font color) for the cell
     * @param foreColorIndex The foreground color (font color) index
     */
	public void setForeColorIndex(short foreColorIndex) {
    	if(foreColorIndex < 0x8 || foreColorIndex > 0x40) {
			throw new FrameworkException("Valid indexes for the Excel custom palette are from 0x8 to 0x40 (inclusive)!");
		}
    	
    	this.foreColorIndex = foreColorIndex;
    }
	
	/**
	 * Function to get whether the cell contents are in bold or not
	 * @return Boolean variable indicating whether the cell contents are in bold or not
	 */
	public boolean isBold() {
		return bold;
	}
    
	/**
	 * Function to set whether the cell contents should be in bold or not
	 * @param bold Boolean variable indicating whether the cell contents should be in bold or not
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	/**
	 * Function to get whether the cell contents are in italics or not
	 * @return Boolean variable indicating whether the cell contents are in italics or not
	 */
	public boolean isItalics() {
		return italics;
	}
	
	/**
	 * Function to set whether the cell contents should be in italics or not
	 * @param bold Boolean variable indicating whether the cell contents should be in italics or not
	 */
	public void setItalics(boolean italics) {
		this.italics = italics;
	}
	
	/**
	 * Function to get whether the cell contents are centered or not
	 * @return Boolean variable indicating whether the cell contents are centered or not
	 */
	public boolean isCentered() {
		return centered;
	}
	
	/**
	 * Function to set whether the cell contents should be centered or not
	 * @param bold Boolean variable indicating whether the cell contents should be centered or not
	 */
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
}