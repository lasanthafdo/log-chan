package org.logchan.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.logchan.core.SystemConstants;

public class HTTPDLogModeler implements LogModelable {
	private SourceApplication srcApp;
	private WebServerLog webLog;

	public HTTPDLogModeler() {
		srcApp = new SourceApplication("Web Server/HTTP Daemon");
		webLog = new WebServerLog("");
	}

	@Override
	public List<Object> getPopulatedModel(Map<String, Object> metaData) {
		List<Object> inputList = new ArrayList<Object>();

		webLog.setFilename((String) metaData.get(SystemConstants.LOG_FILENAME));
		webLog.setFileSize((Integer) metaData
				.get(SystemConstants.TOT_BYTES_READ));
		webLog.setLineCount((Integer) metaData
				.get(SystemConstants.TOT_LINE_COUNT));
		webLog.setParsedLineCount((Integer) metaData
				.get(SystemConstants.TOT_LINE_PARSED));
		srcApp.setStandardLogRate(2000);
		inputList.add(webLog);
		inputList.add(srcApp);

		return inputList;
	}

}
