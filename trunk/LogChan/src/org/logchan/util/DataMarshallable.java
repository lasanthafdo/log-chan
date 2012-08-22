package org.logchan.util;

import java.util.List;
import java.util.Map;

public interface DataMarshallable {
	public Map<String, Integer> getDataSet(List<String[]> messages, Map<String, Object> metaMap);
}
