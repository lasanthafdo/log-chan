package org.logchan.formats;

public interface LogFormattable {
	public void setFormatType(int type);
	public int getFormatType();
	
	public void setRegex(String regex);
	public String getRegex();
	
}
