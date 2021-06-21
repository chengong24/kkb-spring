package com.kkb.mybatis.utils;

import com.kkb.mybatis.sqlsource.KKBParameterMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理#{}中的参数的
 * 
 * @author 灭霸詹
 *
 */
public class KKBParameterMappingTokenHandler implements KKBTokenHandler {
	private List<KKBParameterMapping> parameterMappings = new ArrayList<>();

	// content是参数名称
	// content 就是#{}中的内容
	@Override
	public String handleToken(String content) {
		parameterMappings.add(buildParameterMapping(content));
		return "?";
	}

	private KKBParameterMapping buildParameterMapping(String content) {
		KKBParameterMapping parameterMapping = new KKBParameterMapping(content);
		return parameterMapping;
	}

	public List<KKBParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<KKBParameterMapping> parameterMappings) {
		this.parameterMappings = parameterMappings;
	}

}
