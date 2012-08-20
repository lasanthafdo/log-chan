package org.logchan.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.logchan.formats.HTTPDLogFormat;
import org.logchan.model.DefaultModelHandler;
import org.logchan.model.HTTPDLogModeler;
import org.logchan.model.ModelHandleable;
import org.logchan.reports.ResultInterpretable;
import org.logchan.reports.WebLogInterpreter;
import org.logchan.rules.RuleManager;

public class DefaultFlowController implements FlowControllable {

	private static volatile DefaultFlowController instance = null;
	private final String header = "IP\t User\t Login\t Date-Time\t URL\t\t\t Response Code\t Bytes\t Referrer\t\t\t Client";

	private List<String[]> messages;
	private List<Object> ruleResults;
	private Map<String, Object> metaMap;
	private LogParseable parser;
	private LogReader logReader;
	private InputStream iStream = null;
	private ModelHandleable modelHandler = null;
	private RuleManager ruleManager;
	private ResultInterpretable resultInterpreter = null;

	public static DefaultFlowController getInstance() {
		if (instance == null) {
			instance = new DefaultFlowController();
		}

		return instance;
	}

	private DefaultFlowController() {
		parser = new ApacheLogParser();
		logReader = new LogReader();
		metaMap = null;
		messages = null;
		modelHandler = new DefaultModelHandler();
		ruleManager = new RuleManager();
		resultInterpreter = WebLogInterpreter.getInstance();
	}

	@Override
	public List<String[]> parseFile(String filename, String formatPattern)
			throws IOException {
		messages = null;
		iStream = null;
		if (filename != null) {
			parser.setLogFormat(new HTTPDLogFormat(SystemConstants.HTTPD_NCSA,
					formatPattern));
			parser.setMatchMode(SystemConstants.MATCH_FROM_START);
			iStream = logReader.getInputStream(filename);
			messages = parser.parseLog(iStream);

			metaMap = parser.getMetaData();
			metaMap.put(SystemConstants.LOG_FILENAME, filename);
		}

		return messages;
	}

	@Override
	public void printParsedOutput() {
		if (messages != null && !messages.isEmpty()) {
			System.out.println(header);
			for (String[] message : messages) {
				System.out.println(message[0] + "\t" + message[1] + "\t"
						+ message[2] + "\t" + message[3] + "\t" + message[4]
						+ "\t" + message[5] + "\t" + message[6] + "\t"
						+ message[7] + "\t" + message[8]);
			}
		}
	}

	@Override
	public void printMetaData() {
		Map<String, Object> metD = parser.getMetaData();
		if (!metD.isEmpty()) {
			Set<String> keySet = metD.keySet();
			for (String key : keySet) {
				System.out.println(key + ": " + metD.get(key).toString());
			}
		}
	}

	@Override
	public void processRules() {
		if (metaMap != null) {
			modelHandler.setMetaData(metaMap);
			List<Object> inputList = modelHandler
					.getInputList(new HTTPDLogModeler());
			ruleResults = ruleManager.getRuleResults(inputList);
		}
	}

	@Override
	public void generateRecommendations() {
		if (ruleResults != null) {
			metaMap.put(SystemConstants.REC_LIST, resultInterpreter
					.getInterpretedRecommendations(ruleResults));
		}
	}

	@Override
	public Map<String, Object> getOutputData() {
		return metaMap;
	}
}
