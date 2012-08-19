package org.logchan.model;

import java.util.List;
import java.util.Map;

public class DefaultModelHandler implements ModelHandleable {

	private Map<String, String> metaData = null;
	private List<Object> inputList = null;
	
	@Override
	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	@Override
	public List<Object> getInputList() {
		// TODO Auto-generated method stub
		return null;
	}

}
