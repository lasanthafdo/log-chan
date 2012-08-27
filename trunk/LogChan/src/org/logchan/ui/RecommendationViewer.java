package org.logchan.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jfree.chart.ChartPanel;
import org.logchan.core.SystemConstants;
import org.logchan.reports.Recommendation;

public class RecommendationViewer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7862081874928496731L;

	private JTextPane recContentArea;
	private JButton export;
	private JButton close;
	private Map<String, Object> dataMap;

	public RecommendationViewer(Map<String, Object> recMap) {
		this.dataMap = recMap;
		initialize();
	}

	public void populateRecommendations() {
		StyledDocument doc = getRecommendationsArea().getStyledDocument();
		getRecommendationsArea().setText("");
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, "Verdana");
		StyleConstants.setItalic(attribs, true);
		StyleConstants.setBold(attribs, true);

		try {
			Object obj = dataMap.get(SystemConstants.REC_LIST);
			if (obj instanceof List<?>) {
				List<Recommendation> recList = (List<Recommendation>) obj;
				for (Recommendation rec : recList) {
					if(rec.isStatusOK()) {
						StyleConstants.setForeground(attribs, Color.BLUE);
					} else {
						StyleConstants.setForeground(attribs, Color.BLACK);
					}
					doc.insertString(doc.getLength(),
							rec.getRecommendationMsg() + "\n", attribs);
				}

			}
			if (doc.getLength() == 0) {
				doc.insertString(0, "No recommendations!!!\n", attribs);
			}

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.validate();
	}

	public void addChart(ChartPanel chartPanel) {
		JPanel chartJP = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.9;
		constraints.weighty = 0.7;
		chartJP.add(chartPanel, constraints);
		chartJP.validate();
		constraints.gridy = 2;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		constraints.insets = new Insets(5, 5, 5, 5);
		this.add(chartJP, constraints);
	}

	public void addInfoPanel() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.weightx = 0.2;
		constraints.weighty = 0.2;
		constraints.gridy = 0;
		constraints.gridx = 2;
		constraints.gridheight = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		this.add(getInfoPanel(), constraints);		
	}
	
	private void initialize() {
		this.setTitle("Recommendations");
		this.setSize(800, 800);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel textPanel = new JPanel(new GridBagLayout());
		JLabel recLabel = new JLabel("Recommendations");
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 10, 0);
		constraints.weightx = 0.9;
		constraints.weighty = 0;
		textPanel.add(recLabel, constraints);
		constraints.gridy = 1;
		constraints.weighty = 0.5;
		textPanel.add(getRecommendationsArea(), constraints);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(15, 15, 5, 15);
		this.add(textPanel, constraints);
		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		this.add(getButtons(), constraints);
	}

	private JPanel getButtons() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(2, 5, 2, 5);
		panel.add(getExportButton(), constraints);
		constraints.gridx = 1;
		panel.add(getCloseButton(), constraints);
		return panel;
	}

	private JPanel getInfoPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		int currentY = 0;
		constraints.anchor = GridBagConstraints.WEST;
		panel.add(
				new JLabel("Total Lines: "
						+ dataMap.get(SystemConstants.TOT_LINE_COUNT)),
				constraints);
		constraints.gridy = ++currentY;
		panel.add(
				new JLabel("Total Lines Parsed: "
						+ dataMap.get(SystemConstants.TOT_LINE_PARSED)),
				constraints);
		constraints.gridy = ++currentY;
		panel.add(new JLabel("Avg Line Width: " + dataMap.get(SystemConstants.AVG_BYTES_PER_LINE) + " bytes"), constraints);
		constraints.gridy = ++currentY;
		panel.add(new JLabel("Total Bytes Read: " + dataMap.get(SystemConstants.TOT_BYTES_READ) + " bytes"), constraints);
		if(dataMap.get(SystemConstants.IDENTIFIED_COL) != null) {
			constraints.gridy = ++currentY;
			panel.add(new JLabel("Identified no of Columns: " + dataMap.get(SystemConstants.IDENTIFIED_COL)), constraints);
		}
		constraints.gridy = ++currentY;
		panel.add(new JLabel("Maximum Column No: " + dataMap.get(SystemConstants.MAX_COL)), constraints);
		constraints.gridy = ++currentY;
		panel.add(new JLabel("Mininmum Column No: " + dataMap.get(SystemConstants.MIN_COL)), constraints);

		return panel;
	}

	private JButton getExportButton() {
		if (export == null) {
			export = new JButton("Export");
		}
		return export;
	}

	private JButton getCloseButton() {
		if (close == null) {
			close = new JButton("Close");
			close.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					RecommendationViewer.this.setVisible(false);
				}
			});
		}
		return close;
	}

	private JTextPane getRecommendationsArea() {
		if (recContentArea == null) {
			recContentArea = new JTextPane();
			recContentArea.setEditable(false);
		}
		return recContentArea;
	}

}
