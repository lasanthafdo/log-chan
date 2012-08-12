package org.logchan.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultFlowController implements FlowControllable {

	private static volatile DefaultFlowController instance = null;
	private final String header = "IP\t User\t Login\t Date-Time\t URL\t\t\t Response Code\t Bytes\t Referrer\t\t\t Client";
	
	private List<String[]> messages;
	private LogParseable parser;
	private LogReader logReader;
	private InputStream iStream = null;
	
	public static DefaultFlowController getInstance() {
		if(instance == null) {
			instance = new DefaultFlowController();
		}
		
		return instance;
	}
	
	private DefaultFlowController() {
		parser = new ApacheLogParser();
		logReader = new LogReader();
	}

	@Override
	public List<String[]> parseFile(String filename) throws IOException {
		messages = null;
		iStream = null;
		if (filename != null) {
			iStream = logReader.getInputStream(filename);
			messages = parser.parseLog(iStream);
		}
		
		return messages;
	}
	
	@Override
	public void printParsedOutput() {
		if(messages != null && !messages.isEmpty()) {
			System.out.println(header);
			for(String[] message: messages) {
				System.out.println(message[0] + "\t" + message[1] + "\t" + message[2] + "\t" + 
						message[3] + "\t" + message[4] + "\t" + message[5] + "\t" + 
						message[6] + "\t" + message[7] + "\t" + message[8]);
			}
		}
	}
	
	@Override
	public void printMetaData() {
		Map<String, String> metD = parser.getMetaData();
		if(!metD.isEmpty()) {
			Set<String> keySet = metD.keySet();
			for(String key: keySet) {
				System.out.println(key + ": " + metD.get(key));
			}
		}
	}
}
