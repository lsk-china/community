package com.lsk.community.back.common.authz.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class SpringUtil implements ApplicationContextAware{
	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}
	public static <T> T getBean(String name,Class<T> type){
		return applicationContext.getBean(name,type);
	}
	public static <T> T getBean(Class<T> type){
		return applicationContext.getBean(type);
	}
}
