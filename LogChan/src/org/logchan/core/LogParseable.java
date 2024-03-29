package org.logchan.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.logchan.formats.LogFormattable;

/**
 * All Parsers implement this interface to get access to different parsers
 * @author lasantha
 *
 */
public interface LogParseable {
	/**
	 * Returns a {@link List} of {@link String} arrays that contains the parsed data. Each array
	 * will correspond to a single log entry
	 * @param is
	 * 	The input stream that has to be parsed
	 * @return
	 * 	A List of string arrays that contains the output of parsing or {@code null} if parsing is
	 * 	unsuccessful.
	 * @throws IOException
	 */
	public List<String[]> parseLog(InputStream is) throws IOException;
	
	/**
	 * Returns the meta data gathered from parsing such as the line count, average bytes per line,
	 * log file size etc. The returned object will be a {@link Map} with both keys and values as String.
	 * The map will be empty if the parsing is not successful.
	 * @return
	 * 	A Map<String,String> that contains the meta data for the most recent parsing
	 */
	public Map<String, Object> getMetaData();
	
	/**
	 * Sets the match mode or the parsing mode to a strict matching or linear matching.
	 * Available modes are {@link SystemConstants.MATCH_ENTIRE_REGION},{@link SystemConstants.MATCH_FROM_START}
	 * and {@link SystemConstants.MATCH_TO_FIND} from most strict to the least strict
	 * @param mode
	 * 	the mode to parse the log file
	 */
	public void setMatchMode(int mode);
	
	/**
	 * Sets the log format for parsing.
	 * @param format
	 * 	The format for parsing. Must implement {@code LogFormattable}
	 */
	public void setLogFormat(LogFormattable format);
	
	/**
	 * Derives the column types of the log file and returns a vector.
	 * @param is
	 * 	the input stream of the log file
	 * @return
	 * 	a vector containing the identified column types.
	 */
	public Vector<Class<?>> deriveColumnTypes(InputStream is);
}
