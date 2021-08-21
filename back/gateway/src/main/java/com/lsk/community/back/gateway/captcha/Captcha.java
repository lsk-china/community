package com.lsk.community.back.gateway.captcha;

import com.google.code.kaptcha.Producer;
import com.lsk.community.back.common.redis.RedisComponent;
import com.lsk.community.back.common.response.StatusCode;
import com.lsk.community.back.common.utils.SecurityUtil;
import com.lsk.community.back.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.ClientHttpResponseStatusCodeException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class Captcha {
	@Autowired
	private Producer producer;
	@Autowired
	private RedisComponent redis;

	public Map<String, String> generateCaptcha() throws IOException {     // 返回值：验证码ID，Base64编码过的验证码图片
		String codeID = UUID.randomUUID().toString();
		// 生成验证码
		String codeText = producer.createText();
		BufferedImage codeImage = producer.createImage(codeText);
		// 编码图片
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(codeImage, "jpeg", outputStream);
		String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
		// 保存验证码信息到redis
		redis.set(codeID + "-CODETEXT", codeText, 600);
		Map<String, String> result = new HashMap<>();
		result.put(codeID, base64Image);
		return result;
	}
	public String checkCaptcha(String codeID, String codeText, String targetURL, String clientIP) {
		if (!StringUtil.noEmpty(codeID, codeText, targetURL)) {
			throw new StatusCode(400, "Arguments mustn't be empty");
		}
		String redisCodeText = redis.get(codeID + "-CODETEXT", String.class);
		redis.delete(codeID + "-CODETEXT");
		log.info("redisText: " + redisCodeText);
		log.info("clientText: " + codeText);
		if (!codeText.equals(redisCodeText)) {
			throw new StatusCode(403, "Incorrect code text");
		}
		String requestKey = SecurityUtil.md5(UUID.randomUUID().toString());
		String keyID = SecurityUtil.md5(clientIP + "-" + targetURL);
		redis.set(keyID + "-REQKEY", requestKey, 60);
		log.info("CreateReqKey: clientIP: " + clientIP + " requestKey: " + requestKey + " targetURL: " + targetURL);
		return requestKey;
	}
	public boolean checkRequestKey(String clientIP, String requestKey, String targetURL) {
        log.info("CheckReqKey: clientIP: " + clientIP + " requestKey: " + requestKey + " targetURL: " + targetURL);
		if (!StringUtil.noEmpty(clientIP, requestKey, targetURL)) {
            log.info("Arguments mustn't be empty.");
			return false;
		}
		String keyID = SecurityUtil.md5(clientIP + "-" + targetURL);
		String redisReqKey = redis.get(keyID + "-REQKEY", String.class);
		redis.delete(keyID);
		return requestKey.equals(redisReqKey);
	}
}
