package org.logchan.reports;

import java.util.Map;

import org.jfree.chart.JFreeChart;

public interface Chartable {
	JFreeChart createChart(Map<String, Integer> dataMap);
}
