package org.logchan.core;

import java.util.HashMap;
import java.util.Map;

public class SystemConstants {
	public static final int MATCH_ENTIRE_REGION = 0;
	public static final int MATCH_FROM_START = 1;
	public static final int MATCH_TO_FIND = 2;

	public static final String TOT_LINE_COUNT = "Total Line Count";
	public static final String TOT_LINE_PARSED = "Total Lines Parsed";
	public static final String TOT_BYTES_READ = "Total Bytes Read";
	public static final String AVG_BYTES_PER_LINE = "Average Bytes per Line";
	public static final String MAX_COL = "Maximum Columns";
	public static final String MIN_COL = "Minimum Columns";	
	public static final String IDENTIFIED_COL = "Identified No of Columns";
	public static final String LOG_FILENAME = "Filename";
	public static final String REC_LIST = "Recommendations List";
	public static final String COL_DATA_TYPES = "Column Data Types";
	public static final String LOG_TYPE = "Log File Type";
	public static final String LOG_DELIMITER = "Delimiter";
	public static final String LOG_NULL_CHAR = "Null Char";
	public static final String DERIVED_REGEX = "Regex";
	public static final String TIMESTAMP_COL = "Column Index of Timestamp";
	public static final String PARSE_TIME = "Time taken for parsing";
	
	public static final int UNKNOWN_FORMAT = 0;
	public static final int HTTPD_NCSA = 1;

	public static final String META_DATA_HEAD = "/~BEGIN META DATA~/";
	public static final String META_DATA_END = "/~END META DATA~/";
	
	public static final Map<Character, Character> ESCAPE_CHARS = new HashMap<Character, Character>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2864195449391231854L;

		{
			put('[',']');
			put('"','"');
			put('(',')');
			put('{','}');
		}
	};
	
	public static final Map<Character, String[]> REGEX_ESCAPE_CHARS = new HashMap<Character, String[]>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -91105324597276504L;

		{
			put('[', new String[] { "\\[", "\\]" });
			put('"', new String[] { "\"", "\"" });
		}
	};
}
