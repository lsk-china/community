package com.lsk.community.back.gateway.configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

/**
 * Beans
 * 提供全局使用的Bean
 */
@Configuration
public class Beans {
	@Bean
	public Producer producer() {      // 验证码生成器
		Properties kaptchaProperties = new Properties();
		kaptchaProperties.put("kaptcha.border", "no");
		kaptchaProperties.put("kaptcha.textproducer.char.length","4");
		kaptchaProperties.put("kaptcha.image.height","50");
		kaptchaProperties.put("kaptcha.image.width","150");
		kaptchaProperties.put("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.ShadowGimpy");
		kaptchaProperties.put("kaptcha.textproducer.font.color","black");
		kaptchaProperties.put("kaptcha.textproducer.font.size","40");
		kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
		//kaptchaProperties.put("kaptcha.noise.impl","com.google.code.kaptcha.impl.DefaultNoise");
		kaptchaProperties.put("kaptcha.textproducer.char.string","acdefhkmnprtwxy2345678");
		Config config = new Config(kaptchaProperties);
		return config.getProducerImpl();
	}
	@Bean
	public RestTemplate restTemplate() {     // 模块间请求用的RestTemplate
		return new RestTemplate();
	}
}
