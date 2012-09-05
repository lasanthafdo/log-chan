package org.logchan.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.logchan.formats.GenericLogFormat;
import org.logchan.formats.HTTPDLogFormat;
import org.logchan.formats.LogFormattable;
import org.logchan.model.DefaultModelHandler;
import org.logchan.model.GenericLogModeler;
import org.logchan.model.HTTPDLogModeler;
import org.logchan.model.LogFile;
import org.logchan.model.ModelHandleable;
import org.logchan.rules.RuleManager;
import org.logchan.templates.TemplateDiscoverable;
import org.logchan.templates.TimeStampBasedDiscoverer;
import org.logchan.util.DataMarshallable;
import org.logchan.util.GenericMarshaller;
import org.logchan.util.HTTPDMarshaller;

public class DefaultFlowController implements FlowControllable {

	private static DefaultFlowController instance = null;
	private final String header = "IP\t User\t Login\t Date-Time\t URL\t\t\t Response Code\t Bytes\t Referrer\t\t\t Client";

	private List<String[]> messages;
	private List<Object> ruleResults;
	private Map<String, Object> metaMap;
	private LogParseable parser;
	private LogReader logReader;
	private InputStream iStream = null;
	private ModelHandleable modelHandler = null;
	private RuleManager ruleManager;

	private DataMarshallable dataMarshaller = null;
	private TemplateDiscoverable tempDiscoverer = null;

	public static DefaultFlowController getInstance() {
		if (instance == null) {
			instance = new DefaultFlowController();
		}

		return instance;
	}

	private DefaultFlowController() {
		parser = null;
		logReader = new LogReader();
		metaMap = new HashMap<String, Object>();
		messages = null;
		modelHandler = new DefaultModelHandler();
		ruleManager = new RuleManager();
		dataMarshaller = null;
		tempDiscoverer = new TimeStampBasedDiscoverer();
	}

	@Override
	public List<String[]> parseFile(String filename, String formatPattern)
			throws IOException {
		Vector<Class<?>> columnTypes = null;
		messages = null;
		iStream = null;
		if (filename != null) {
			LogFormattable format = null;
			if (metaMap.get(SystemConstants.DERIVED_REGEX) != null) {
				format = new GenericLogFormat(formatPattern);
				parser = new RegexLogParser(format, metaMap);
			} else {
				format = new HTTPDLogFormat(SystemConstants.HTTPD_NCSA,
						formatPattern);
				parser = new ApacheLogParser();
				parser.setLogFormat(format);
				parser.setMatchMode(SystemConstants.MATCH_FROM_START);

				metaMap.put(SystemConstants.LOG_TYPE, format.getFormatType());
				metaMap.put(SystemConstants.LOG_DELIMITER,
						format.getDelimiter());
				metaMap.put(SystemConstants.LOG_NULL_CHAR,
						format.getLogNullChar());
			}
			iStream = logReader.getInputStream(filename);
			messages = parser.parseLog(iStream);
			if (metaMap.get(SystemConstants.COL_DATA_TYPES) == null) {
				columnTypes = parser.deriveColumnTypes(logReader
						.getInputStream(filename));
				metaMap.put(SystemConstants.COL_DATA_TYPES, columnTypes);
			}
			metaMap.putAll(parser.getMetaData());
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
			List<Object> inputList;
			if(metaMap.get(SystemConstants.LOG_TYPE).equals(SystemConstants.HTTPD_NCSA))
				inputList = modelHandler.getInputList(new HTTPDLogModeler());
			else
				inputList = modelHandler.getInputList(new GenericLogModeler());
			
			ruleResults = ruleManager.getRuleResults(inputList);
			if(!ruleResults.isEmpty()) {
				for(Object obj : ruleResults) {
					if(obj instanceof LogFile) {
						metaMap.put(SystemConstants.REC_LIST, ((LogFile)obj).getRecList());
						break;
					}
				}
			}
		}
	}

	@Override
	public Map<String, Object> getOutputData() {
		return metaMap;
	}

	@Override
	public Map<Integer, Integer> getTimeMarshalledData(List<String[]> messages) {
		if(dataMarshaller == null) {
			if(metaMap.get(SystemConstants.LOG_TYPE).equals(SystemConstants.HTTPD_NCSA)) {
				dataMarshaller = new HTTPDMarshaller();
			}
			else if(metaMap.get(SystemConstants.TIMESTAMP_COL) != null) {
				dataMarshaller = new GenericMarshaller();
			}
		}
		
		if (dataMarshaller != null) {
			return dataMarshaller.getDataSet(messages, metaMap);
		}

		return null;
	}

	@Override
	public void saveFile(String filename) throws IOException {
		LogWriter.writeToFile(filename, messages, metaMap);
	}

	@Override
	public String getDerivedRegex(String filename) throws IOException {
		metaMap.putAll(tempDiscoverer.discoverTemplate(filename, new File(
				filename).length()));
		return (String) metaMap.get(SystemConstants.DERIVED_REGEX);
	}

	@Override
	public void reset() {
		parser = null;
		metaMap.clear();
		metaMap = new HashMap<String, Object>();
		messages = null;
		dataMarshaller = null;
	}
}
