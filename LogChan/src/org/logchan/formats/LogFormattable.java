package org.logchan.formats;

public interface LogFormattable {
	public void setParams(String delimiter, String formatName, String logNullChar);
	public void setRegex(String regex);
	
	public int getFormatType();	
	public String getRegex();
	public String getFormatName();
	public String getDelimiter();
	public String getLogNullChar();
}
