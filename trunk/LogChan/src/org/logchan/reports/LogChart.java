package org.logchan.reports;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LogChart implements Chartable {
	private XYSeries values;
	
	@Override
	public ChartPanel createChart(Map<Integer, Integer> dataMap) {
		// TODO Auto-generated method stub
		JFreeChart chart = null;
		//DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		int i = 10;
		values = new XYSeries("CPU",true,false);
		
		for(Integer key:dataMap.keySet()) {
            System.out.println(key);
			values.add(i,dataMap.get(key));
			i=i+10;
		}

//		System.out.println(dataMap.keySet().size());
	    chart = new JFreeChart("CPU Usage", getPlot());   
	    chart.removeLegend();
	    return new ChartPanel(chart, true, true, true, true, true);
		
//		chart = ChartFactory.createBarChart("Log Generation Rate", 
//				"Time", "Count", dataSet, PlotOrientation.VERTICAL, false, true, false);
//		chart.setBackgroundPaint(Color.BLACK);		
//		setPlot(chart);
	}	 

	  private XYPlot getPlot() {
		  	  XYPlot plot = new XYPlot(new XYSeriesCollection(values), getDomainAxis(), getRangeAxis(), getRenderer());
		      plot.setBackgroundPaint(Color.BLACK);
		      plot.setDomainGridlinePaint(new Color(25, 255, 52));
		      plot.setRangeGridlinePaint(new Color(25, 255, 52));
		      plot.setOutlinePaint(Color.BLACK);
		      plot.setDomainMinorGridlinesVisible(true);
		      plot.setDomainMinorGridlinePaint(new Color(14,124,35));
		      plot.setRangeMinorGridlinesVisible(true);
		      plot.setRangeMinorGridlinePaint(new Color(14,124,35));
		  	return plot;
		  }
		  private NumberAxis getDomainAxis() {
		  	NumberAxis domainAxis = new NumberAxis("Last 100 Sec.");
		  	domainAxis.setAutoRange(false);
		  	domainAxis.setAxisLinePaint(Color.BLACK);
		  	domainAxis.setNegativeArrowVisible(false);
		  	domainAxis.setUpperBound(1000);
		  	return domainAxis;
		  }
		  private NumberAxis getRangeAxis() {
		  	NumberAxis rangeAxis = new NumberAxis("CPU usage ( % )");
		      rangeAxis.setAutoRange(false);
		      rangeAxis.setAxisLinePaint(Color.BLACK);
		      rangeAxis.setNegativeArrowVisible(false);
		      rangeAxis.setUpperBound(100);
		      rangeAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		      rangeAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
		      rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		  	return rangeAxis;
		  }
		   private XYItemRenderer getRenderer(){
		  	 XYItemRenderer renderer = new StandardXYItemRenderer();
		  	 renderer.setSeriesPaint(0,  new Color(0,255,0));
		  	 return renderer;
		   }


}
