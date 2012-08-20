package org.logchan.formats;

public class HTTPDLogFormat implements LogFormattable {

	private String regex;
	private int formatType;
	
	public HTTPDLogFormat(int type, String regex) {
		this.formatType = type;
		this.regex = regex;
	}
	
	@Override
	public void setFormatType(int type) {
		// TODO Auto-generated method stub
		this.formatType = type;
	}

	@Override
	public void setRegex(String regex) {
		// TODO Auto-generated method stub
		this.regex = regex;
	}

	@Override
	public int getFormatType() {
		// TODO Auto-generated method stub
		return formatType;
	}

	@Override
	public String getRegex() {
		// TODO Auto-generated method stub
		return regex;
	}

}
