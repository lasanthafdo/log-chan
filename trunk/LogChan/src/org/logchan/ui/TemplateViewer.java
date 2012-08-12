package org.logchan.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.logchan.core.ApacheLogParser;
import org.logchan.model.TableData;

public class TemplateViewer extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6755248784737848181L;
	
	private JButton save;
	private JButton close;
	List<String[]> messages;

	public TemplateViewer(List<String[]> messages){
		this.messages = messages;
		initialize();		
	}
	
	private void initialize(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Col 1");
		columnNames.add("Col 2");
		columnNames.add("Col 3");
		columnNames.add("Col 4");
		columnNames.add("Col 5");
		columnNames.add("Col 6");
		columnNames.add("Col 7");
		columnNames.add("Col 8");
		columnNames.add("Col 9");
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		for(String[] message:messages){
			Vector<String> element = new Vector<String>();
			for(int i=0;i<ApacheLogParser.NUM_FIELDS;i++){
				element.add(i,message[i]);	
			}
			data.add(element);
		}		
		this.add(new Template(data,columnNames),constraints);
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
		panel.add(getSaveButton(),constraints);
		constraints.gridx = 1;
		panel.add(getCloseButton(),constraints);
		return panel;
	}
	
	private JButton getSaveButton(){
		if(save == null){
			save = new JButton("Save");
		}
		return save;
	}
	
	private JButton getCloseButton(){
		if(close == null){
			close = new JButton("Close");
		}
		return close;
	}
	
	
	private class Template extends JTable{		
		
	   /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	private Template(Vector<Vector<String>> data, Vector<String> columnNames){	   
		   TableData model = new TableData(data,columnNames);
		   this.setModel(model.getModel());
	   }
	}
}
