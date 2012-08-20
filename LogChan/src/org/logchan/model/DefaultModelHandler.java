package org.logchan.model;

import java.util.List;
import java.util.Map;

public class DefaultModelHandler implements ModelHandleable {

	private Map<String, Object> metaData = null;
	
	@Override
	public void setMetaData(Map<String, Object> metaData) {
		this.metaData = metaData;
	}

	@Override
	public List<Object> getInputList(LogModelable logModeler) {
		return logModeler.getPopulatedModel(metaData);
	}

}
