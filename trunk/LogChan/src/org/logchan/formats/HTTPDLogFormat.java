package org.logchan.formats;

public class HTTPDLogFormat implements LogFormattable {

	private String regex;
	private String delimiter;
	private String formatName;
	private String logNullChar;
	private int formatType;

	@Override
	public void setParams(String delimiter, String formatName,
			String logNullChar) {
		this.delimiter = delimiter;
		this.formatName = formatName;
		this.logNullChar = logNullChar;
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public String getFormatName() {
		return formatName;
	}

	@Override
	public String getLogNullChar() {
		return logNullChar;
	}

	public HTTPDLogFormat(int type, String regex) {
		this.formatType = type;
		this.regex = regex;
		//These are just defaults
		this.delimiter = " ";
		this.logNullChar = "-";
		this.formatName = "NCSA/HTTP Common Format";
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
