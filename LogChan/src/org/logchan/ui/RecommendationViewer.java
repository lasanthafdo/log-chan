package org.logchan.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class RecommendationViewer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7862081874928496731L;
	
	private JButton export;
	private JButton close;
	private Map<String, String> recommendationMap;

	public RecommendationViewer(Map<String, String> recMap) {
		// TODO Auto-generated constructor stub
		this.recommendationMap = recMap;
		initialize();
	}
		

	
	private void initialize(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
	
		constraints.gridwidth = 1;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.SOUTHEAST;
		this.add(new JPanel(),constraints);
		constraints.weightx = 0;
		constraints.gridx = 1;
		this.add(getButtons(),constraints);
	}
	
	private JPanel getButtons(){
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new  GridBagConstraints();
		panel.add(getExportButton(),constraints);
		constraints.gridx = 1;
		panel.add(getCloseButton(),constraints);
		return panel;
	}
	
	private JButton getExportButton(){
		if(export == null){
			export = new JButton("Export");
		}
		return export;
	}
	
	private JButton getCloseButton(){
		if(close == null){
			close = new JButton("Close");
		}
		return close;
	}
	

}
