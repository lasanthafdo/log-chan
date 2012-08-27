package org.logchan.model;

import java.util.ArrayList;
import java.util.List;

import org.logchan.reports.Recommendation;

public abstract class LogFile {
	private int fileSize;
	private int lineCount;
	private int parsedLineCount;
	private int dimensionality;
	private String filename;
	
	private List<Recommendation> recList;
	
	public LogFile(String filename) {
		this.filename = filename;
		recList = new ArrayList<Recommendation>();
	}

	public int getFileSize() {
		return fileSize;
	}

	public List<Recommendation> getRecList() {
		return recList;
	}
	
	public void addRecommendation(Recommendation rec) {
		recList.add(rec);
	}
	
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getDimensionality() {
		return dimensionality;
	}

	public void setDimensionality(int dimensionality) {
		this.dimensionality = dimensionality;
	}

	public int getParsedLineCount() {
		return parsedLineCount;
	}

	public void setParsedLineCount(int parsedLineCount) {
		this.parsedLineCount = parsedLineCount;
	}

}
