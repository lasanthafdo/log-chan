package org.logchan.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

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
		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attribs, "Verdana");
		StyleConstants.setItalic(attribs, true);
		
		for(String key: keySet) {
			if(dataMap.get(key) instanceof String) {
				try {
					doc.insertString(0, (String) dataMap.get(key), attribs);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		this.validate();
	}
	
	private void initialize(){
		this.setSize(600, 400);
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel textPanel = new JPanel();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		textPanel.add(getRecommendationsArea(), constraints);
		constraints.gridwidth = 2;
		constraints.insets = new Insets(5, 2, 5, 2);
		this.add(textPanel, constraints);
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
