package com.autopia4j.framework.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autopia4j.framework.core.AutopiaException;

/**
 * Class to manage interactions with MS Word Documents
 * @author vj
 */
public class WordDocumentManager {
	private final Logger logger = LoggerFactory.getLogger(WordDocumentManager.class);
	private final String filePath;
	private final String fileName;
	
	
	/**
	 * Constructor to initialize the Word Document filepath and filename
	 * @param filePath The absolute path where the Word Document is stored
	 * @param fileName The name of the Word Document (without the extension).
	 * Note that .doc files are not supported, only .docx files are supported.
	 */
	public WordDocumentManager(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	
	/**
	 * Function to create a new Word document
	 */
	public void createDocument() {
		XWPFDocument document = new XWPFDocument();
		
		writeIntoFile(document);
	}
	
	private void writeIntoFile(XWPFDocument document) {
		String absoluteFilePath = filePath + Util.getFileSeparator() +
														fileName + ".docx";
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(absoluteFilePath);) {
			document.write(fileOutputStream);
		} catch (IOException e) {
			String errorDescription = "Error while writing into the specified Word document \"" + absoluteFilePath + "\"";
			logger.error(errorDescription, e);
			throw new AutopiaException(errorDescription);
		}
	}
	
	/**
	 * Function to add a picture to the Word document
	 * @param pictureFile the picture {@link File} to be inserted
	 */
	public void addPicture(File pictureFile) {
		CustomXWPFDocument document = openFileForReading();
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun run = paragraph.createRun();
		run.setText(pictureFile.getName());
		
		String id;
		try {
			id = document.addPictureData(new FileInputStream(pictureFile),
												Document.PICTURE_TYPE_PNG);
			
			BufferedImage image = ImageIO.read(pictureFile);
			document.createPicture(id,
						document.getNextPicNameNumber(Document.PICTURE_TYPE_PNG),
						image.getWidth(), image.getHeight());
		} catch (InvalidFormatException | IOException e) {
			String errorDescription = "Exception thrown while adding a picture file to a Word document";
			logger.error(errorDescription, e);
			throw new AutopiaException(errorDescription);
		}
		
		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.addBreak(BreakType.PAGE);
		
		writeIntoFile(document);
	}
	
	private CustomXWPFDocument openFileForReading() {
		String absoluteFilePath = filePath + Util.getFileSeparator() +
														fileName + ".docx";
		
		try (FileInputStream fileInputStream = new FileInputStream(absoluteFilePath);
				CustomXWPFDocument document = new CustomXWPFDocument(fileInputStream);) {
			return document;
		} catch (IOException e) {
			String errorDescription = "Error while opening the specified Word document \"" + absoluteFilePath + "\"";
			logger.error(errorDescription, e);
			throw new AutopiaException(errorDescription);
		}
	}
}