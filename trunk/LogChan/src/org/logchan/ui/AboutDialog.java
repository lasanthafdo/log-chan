package org.logchan.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2680283920640290205L;
	
	public AboutDialog() {
		initialize();
	}
	
	private void initialize() {
		this.setTitle("About Log-Chan");
		this.setSize(400, 400);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel uiPanel = new JPanel(new GridBagLayout());
		JLabel aboutLabel = new JLabel("This tool was developed as a proof of concept\n for Final Year Undergraduate Project\n Department of Computer Science & Engineering\n University of Moratuwa");
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 5, 10, 0);
		constraints.weightx = 0.9;
		constraints.weighty = 0.9;
		uiPanel.add(aboutLabel, constraints);
		constraints.gridy = 1;
		JLabel developers = new JLabel("Developers: Lasantha Fernando, Samila Madusanka");
		uiPanel.add(developers, constraints);
		constraints.gridy = 2;
		JLabel copyrightLabel = new JLabel("Copyright 2012");
		uiPanel.add(copyrightLabel, constraints);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.insets = new Insets(15, 15, 5, 15);
		this.add(uiPanel, constraints);
	}
	

}
