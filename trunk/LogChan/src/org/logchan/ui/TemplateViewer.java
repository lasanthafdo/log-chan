package org.logchan.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.logchan.model.TableData;

public class TemplateViewer extends JDialog{
	private JButton save;
	private JButton close;

	public TemplateViewer(){
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
		columnNames.add(0,"C1");
		columnNames.add(1,"C2");
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> data1 = new Vector<String>();
		data1.add(0,"D1C1");
		data1.add(1,"D1C2");
		data.add(0,data1);
		Vector<String> data2 = new Vector<String>();
		data2.add(0,"D2C2");
		data2.add(1,"D2C2");
		data.add(1,data2);
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
		
	   private Template(Vector<Vector<String>> data, Vector<String> columnNames){	   
		   TableData model = new TableData(data,columnNames);
		   this.setModel(model.getModel());
	   }
	}
}
