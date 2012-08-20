package org.logchan.core;

import java.util.HashMap;
import java.util.Map;

public class SystemMappings {
	public static final Map<String, String> EXPRESSION_MAP = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3326915278507712853L;

		{
			put("HTTPD/NCSA", "^([\\d.:]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+|\\-) \"([^\"]+)\" \"([^\"]+)\"");
		}
	};
	
	public static final Integer SIZE_TO_LINES_RATIO_LARGE = 1;
}
