package org.logchan.rules;

import java.util.ArrayList;
import java.util.List;

import org.logchan.model.SourceApplication;
import org.logchan.model.WebServerLog;

public class RuleManager {

	private LogRuleEngine logRuleEngine;
	
	public RuleManager() {
		logRuleEngine = new LogRuleEngine();
	}
	
	public List<Object> getRuleResults(List<Object> inputList) {
		logRuleEngine.setInput(inputList);
		return logRuleEngine.processRulesFromResource("/org/logchan/rules/size_rules.xml");
	}
	
	public void invokeExample() {
		// Then call executeRules on the input objects.

		WebServerLog webLog = new WebServerLog("test.log");
		webLog.setFileSize(5000);
		webLog.setLineCount(3000);

		SourceApplication app = new SourceApplication("Apache Web Server");
		app.setStandardLogRate(2000);

		List<Object> input = new ArrayList<Object>();		
		input.add(webLog);
		input.add(app);
		
		logRuleEngine.setInput(input);
		logRuleEngine.processRulesFromResource("/org/logchan/rules/size_rules.xml");
	}
}
