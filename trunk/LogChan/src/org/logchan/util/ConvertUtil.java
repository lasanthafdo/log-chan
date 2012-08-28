package org.logchan.util;

/**
 * Extracted from http://www.xinotes.org/notes/note/1330/
 * @author alfa
 *
 */
import java.util.*;
import java.lang.reflect.*;

import org.logchan.model.DataType;

import com.jshift.util.collections.linkedhash.LinkedHashMap;

public class ConvertUtil {
	private static Map<String, Class<?>> primitiveMap = new LinkedHashMap<String, Class<?>>();
	static {
		primitiveMap.put(SupportedTypes.T_BYTE, Byte.class);
		primitiveMap.put(SupportedTypes.T_SHORT, Short.class);
		primitiveMap.put(SupportedTypes.T_INTEGER, Integer.class);
		primitiveMap.put(SupportedTypes.T_LONG, Long.class);
		primitiveMap.put(SupportedTypes.TC_JAVA_DATE, DateConverter.class);
		primitiveMap.put(SupportedTypes.T_FLOAT, Float.class);
		primitiveMap.put(SupportedTypes.T_DOUBLE, Double.class);
	}

	/**
	 * Try to convert to destClass type. If it is supported an object of that
	 * type is returned. Otherwise a string is returned.
	 */
	public static DataType convert(String value, Class<?> destClass) {
		DataType dataType = null;
		if ((value == null) || "".equals(value)) {
			return dataType;
		}

		try {
			Method m = destClass.getMethod("valueOf", String.class);
			int mods = m.getModifiers();
			if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) {
				Object retObj = m.invoke(null, value);
				return new DataType(retObj.getClass(), retObj);
			}
		} catch (NoSuchMethodException e) {
			if (destClass == Character.class && value.length() == 1)
				return new DataType(Character.class, Character.valueOf(value
						.charAt(0)));
			else
				e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// Not intended target. Do nothing. But......
			// Boolean has to be handled specially
			if (value.equalsIgnoreCase("true")
					|| value.equalsIgnoreCase("false"))
				return new DataType(Boolean.class, Boolean.valueOf(value));
		}

		return new DataType(String.class, value);
	}

	public static DataType attemptConversion(String param) {
		DataType dataType = null;
		Set<String> keySet = primitiveMap.keySet();
		for (String key : keySet) {
			dataType = ConvertUtil.convert(param, primitiveMap.get(key));
			if (!(dataType.getDataValue() instanceof String)) {
				break;
			}
		}

		return dataType;
	}
	
	public static Class<?> getDataType(String param) {
		return attemptConversion(param).getDataType();
	}

}
