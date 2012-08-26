package org.logchan.formats;

public class GenericLogFormat implements LogFormattable {

	private String regex;
	private String delimiter;
	private String formatName;
	private String logNullChar;
	private int formatType = 0;
	
	public GenericLogFormat(String regex) {
		this.regex = regex;
		this.formatName = "Unknown Format";
	}
	
	@Override
	public void setParams(String delimiter, String formatName,
			String logNullChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRegex(String regex) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFormatType() {
		// TODO Auto-generated method stub
		return this.formatType;
	}

	@Override
	public String getRegex() {
		// TODO Auto-generated method stub
		return this.regex;
	}

	@Override
	public String getFormatName() {
		// TODO Auto-generated method stub
		return this.formatName;
	}

	@Override
	public String getDelimiter() {
		// TODO Auto-generated method stub
		return this.delimiter;
	}

	@Override
	public String getLogNullChar() {
		// TODO Auto-generated method stub
		return this.logNullChar;
	}

}
