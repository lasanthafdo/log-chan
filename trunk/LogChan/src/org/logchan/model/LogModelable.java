package org.logchan.model;

import java.util.List;
import java.util.Map;

public interface LogModelable {

	public List<Object> getPopulatedModel(Map<String, Object> metaData);
}
