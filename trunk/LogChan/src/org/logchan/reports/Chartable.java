package org.logchan.reports;

import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public interface Chartable {
	ChartPanel createChart(Map<Integer, Integer> dataMap);
}
