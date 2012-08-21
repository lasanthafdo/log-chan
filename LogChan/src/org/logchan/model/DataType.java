package org.logchan.model;

public class DataType {
	private Class<?> dataType;
	private Object dataValue;
	
	public DataType(Class<?> dataType, Object dataValue) {
		this.dataType = dataType;
		this.dataValue = dataValue;
	}
	
	public Class<?> getDataType() {
		return dataType;
	}

	public Object getDataValue() {
		return dataValue;
	}


}
