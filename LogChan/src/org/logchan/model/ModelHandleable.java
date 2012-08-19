package org.logchan.model;

import java.util.List;
import java.util.Map;

public interface ModelHandleable {
	public void setMetaData(Map<String, String> metaData);
	public List<Object> getInputList();
}
