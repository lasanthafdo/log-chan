package org.logchan.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.logchan.model.WebServerLog;

public class WebLogInterpreter implements ResultInterpretable {

	private Map<Integer, Recommendation> webRecMap;
	private static WebLogInterpreter instance = null;

	public static WebLogInterpreter getInstance() {
		if (instance == null) {
			instance = new WebLogInterpreter();
		}

		return instance;
	}

	private WebLogInterpreter() {
		webRecMap = new HashMap<Integer, Recommendation>();
	}

	public void addRecommendation(Integer status, Recommendation rec) {
		webRecMap.put(status, rec);
	}

	@Override
	public List<String> getInterpretedRecommendations(List<Object> results) {
		// TODO Auto-generated method stub
		populateRecommendations();		
		List<String> msgList = new ArrayList<String>();
		Iterator<Object> itr = results.iterator();
		while (itr.hasNext()) {
			msgList.add(interpretResult(itr.next()));
		}

		return msgList;
	}

	private void populateRecommendations() {
		addRecommendation(1, new Recommendation(true,
				"The log file size is acceptable to with regard to line count"));
	}

	private String interpretResult(Object resultObj) {
		String recommendationMsg = "No recommendation regarding this property";
		Recommendation rec = null;
			if (resultObj instanceof WebServerLog) {
				rec = webRecMap.get(((WebServerLog) resultObj)
						.getLogSizeStatus());
				recommendationMsg = rec.getRecommendationMsg();
			}

		return recommendationMsg;
	}
}
