package org.logchan.model;

public class WebServerLog extends LogFile {

	private int logSizeStatus;
	
	public WebServerLog(String filename) {
		super(filename);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseLog() {
		// TODO Auto-generated method stub
		
	}

	public int getLogSizeStatus() {
		return logSizeStatus;
	}

	public void setLogSizeStatus(int logSizeStatus) {
		this.logSizeStatus = logSizeStatus;
	}

}
