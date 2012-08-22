package org.logchan.reports;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LogChart implements Chartable {

	public LogChart() {

	}
	
	@Override
	public JFreeChart createChart(Map<String, Integer> dataMap) {
		// TODO Auto-generated method stub
		JFreeChart chart = null;
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for(String key: dataMap.keySet()) {
			dataSet.addValue(dataMap.get(key), "Count", key);
		}
		chart = ChartFactory.createBarChart("Log Generation Rate", 
				"Time", "Count", dataSet, PlotOrientation.VERTICAL, false, true, false);
		
		return chart;
	}

}
