package org.logchan.model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableData {

	private Vector<String> columnNames;
	private Vector<Vector<String>> data;

	public TableData(Vector<Vector<String>> data, Vector<String> columnNames) {
		this.data = data;
		this.columnNames = columnNames;
	}

	public TableModel getModel() {
		return new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -7911761238075532719L;

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

}
