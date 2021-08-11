package com.lsk.community.back.common.authz.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
	public static List<String> createParameterNamesList(Method method) {
		Parameter[] parameters = method.getParameters();
		List<String> result = new ArrayList<>();
		for (Parameter parameter : parameters) {
			result.add(parameter.getName());
		}
		return result;
	}
}
