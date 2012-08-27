package org.logchan.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.logchan.core.SystemConstants;

public class GenericLogModeler implements LogModelable {
	private GenericLog genericLog;
	private SourceApplication srcApp;
	
	public GenericLogModeler() {
		genericLog = new GenericLog("");
		srcApp = new SourceApplication("Generic Application");
	}
	@Override
	public List<Object> getPopulatedModel(Map<String, Object> metaData) {
		List<Object> inputList = new ArrayList<Object>();

		genericLog.setFilename((String) metaData.get(SystemConstants.LOG_FILENAME));
		genericLog.setFileSize((Integer) metaData
				.get(SystemConstants.TOT_BYTES_READ));
		genericLog.setLineCount((Integer) metaData
				.get(SystemConstants.TOT_LINE_COUNT));
		srcApp.setStandardLogRate(10000);
		inputList.add(genericLog);
		inputList.add(srcApp);

		return inputList;
	}

}
