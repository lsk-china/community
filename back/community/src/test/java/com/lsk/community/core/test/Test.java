package com.lsk.community.core.test;

import com.lsk.community.back.common.redis.RedisComponent;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test {
	@Autowired
	private RedisComponent redis;

	@org.junit.Test
	public void test() {
		redis.get("sdasd", Integer.class);
	}
}
