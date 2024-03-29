package org.logchan.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FlowControllable {
	/**
	 * Parses the file given as {@code filename} and returns {@link List} of {@link String} arrays
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public List<String[]> parseFile(String filename, String formatPattern) throws IOException;
	
	/**
	 * Prints the output of the already parsed file.
	 */
	public void printParsedOutput();
	
	/**
	 * Prints the meta data, file characteristics of the log file parsed
	 */
	public void printMetaData();
	
	public void processRules();
	
	public Map<String, Object> getOutputData();
	
	public Map<Integer, Integer> getTimeMarshalledData(List<String[]> messages);
	
	public void saveFile(String filename) throws IOException;
	
	public String getDerivedRegex(String filename) throws IOException;
	
	public void reset();
}
