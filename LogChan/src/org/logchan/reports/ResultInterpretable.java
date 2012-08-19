package org.logchan.reports;

import java.util.List;

public interface ResultInterpretable {
	public List<String> getInterpretedRecommendations(List<Object> results);
}
