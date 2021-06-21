package com.kkb.mybatis.utils;

public class KKBReflectUtils {
	public static Class<?> resolveType(String parameterType) {
		try {
			Class<?> clazz = Class.forName(parameterType);
			return clazz;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
