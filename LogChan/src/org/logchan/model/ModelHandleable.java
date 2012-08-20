package org.logchan.model;

import java.util.List;
import java.util.Map;

public interface ModelHandleable {
	public void setMetaData(Map<String, Object> metaData);
	public List<Object> getInputList(LogModelable logModeler);
}
