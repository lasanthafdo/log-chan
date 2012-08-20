package org.logchan.reports;

public class Recommendation {
	private boolean statusOK = false;
	private String recommendationMsg;
	private int recommendationIndex;

	public Recommendation(boolean statusOK, String msg) {
		this.statusOK = statusOK;
		this.recommendationMsg = msg;
	}
	
	public boolean isStatusOK() {
		return statusOK;
	}

	public void setStatusOK(boolean statusOK) {
		this.statusOK = statusOK;
	}

	public String getRecommendationMsg() {
		return recommendationMsg;
	}

	public void setRecommendationMsg(String recommendationMsg) {
		this.recommendationMsg = recommendationMsg;
	}

	public int getRecommendationIndex() {
		return recommendationIndex;
	}

	public void setRecommendationIndex(int recommendationIndex) {
		this.recommendationIndex = recommendationIndex;
	}

}
