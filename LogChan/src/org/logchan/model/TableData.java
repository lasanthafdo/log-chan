package org.logchan.model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableData{
   
  private Vector<String> columnNames;
  private Vector<Vector<String>> data;
  
  public TableData(  Vector<Vector<String>> data, Vector<String> columnNames ){
	  this.data = data;
	  this.columnNames = columnNames;
  }
  
  public TableModel getModel(){
	  System.out.println("data: " + data.size());
	  System.out.println("cols: " + columnNames.size());
	  return new DefaultTableModel(data, columnNames){
		  @Override
		public boolean isCellEditable(int row, int column) {
			// TODO Auto-generated method stub
			return false;
		}
	  };
  }
	
}
