package org.logchan.rules;

import java.util.List;

public class RuleManager {

	private LogRuleEngine logRuleEngine;
	
	public RuleManager() {
		logRuleEngine = new LogRuleEngine();
	}
	
	public List<Object> getRuleResults(List<Object> inputList) {
		logRuleEngine.setInput(inputList);
		return logRuleEngine.processRulesFromResource("/org/logchan/rules/size_rules.xml");
	}
	
}
