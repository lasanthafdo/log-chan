package org.logchan.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface LogParseable {
	public List<String[]> parseLog(InputStream is) throws IOException;
	public Map<String, String> getMetaData();
}
