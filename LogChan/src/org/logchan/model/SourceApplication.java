package org.logchan.model;

public class SourceApplication {
	private int standardLogRate;
	private String applicationName;
	private int applicationRating;

	public SourceApplication(String name) {
		this.applicationName = name;
	}
	
	public int getStandardLogRate() {
		return standardLogRate;
	}

	public void setStandardLogRate(int standardLogRate) {
		this.standardLogRate = standardLogRate;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public int getApplicationRating() {
		return applicationRating;
	}

	public void setApplicationRating(int applicationRating) {
		this.applicationRating = applicationRating;
	}
	
}
