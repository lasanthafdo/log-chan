package org.logchan.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logchan.model.WebServerLog;

public class WebLogInterpreter implements ResultInterpretable {

	private Map<Integer, Recommendation> webRecMap;
	private static WebLogInterpreter instance = null;
	
	public static WebLogInterpreter getInstance() {
		if(instance == null) {
			instance = new WebLogInterpreter();
		}
		
		return instance;
	}
	
	private WebLogInterpreter() {
		webRecMap = new HashMap<Integer, Recommendation>();
	}
	
	public void addRecommendation(Recommendation rec) {
		webRecMap.put(1, new Recommendation(true, "The log file size is acceptable to with regard to line count"));
	}
	
	@Override
	public List<String> getInterpretedRecommendations(List<Object> results) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	private String interpretResult(Object resultObj) {
		Recommendation rec = null;
		if(resultObj instanceof WebServerLog) {
			rec = webRecMap.get((Integer)((WebServerLog) resultObj).getLogSizeStatus());
		}
		
		return rec.getRecommendationMsg();
	}
}
