package org.logchan.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.jfree.chart.JFreeChart;
import org.logchan.core.SystemConstants;

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
		Set<String> keySet = dataMap.keySet();
		StyledDocument doc = getRecommendationsArea().getStyledDocument();
		getRecommendationsArea().setText("");
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, "Verdana");
		StyleConstants.setItalic(attribs, true);

		try {
			for (String key : keySet) {
				if (key.equals(SystemConstants.REC_LIST)
						&& dataMap.get(key) instanceof List<?>) {
					List<String> recMsgs = (List<String>) dataMap.get(key);
					for (String msg : recMsgs)
						doc.insertString(doc.getLength(), msg + "\n", attribs);
				}
			}
			if(doc.getLength() == 0) {
				doc.insertString(0, "No recommendations!!!\n", attribs);
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.validate();
	}

	public void addChart(JFreeChart chart) {
		ChartPanel cp = new ChartPanel(chart);
		JPanel chartJP = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		chartJP.add(cp, constraints);
		chartJP.validate();
		constraints.gridy = 0;
		constraints.gridx = 2;
		constraints.gridheight = 2;
		constraints.insets = new Insets(5, 5, 5, 5);
		this.add(chartJP,constraints);
	}
	
	private void initialize() {
		this.setTitle("Recommendations");
		this.setSize(800, 600);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel textPanel = new JPanel(new GridBagLayout());
		JLabel recLabel = new JLabel("Recommendations");
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 10, 0);
		constraints.weightx = 0.5;
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
		panel.add(getExportButton(), constraints);
		constraints.gridx = 1;
		panel.add(getCloseButton(), constraints);
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
