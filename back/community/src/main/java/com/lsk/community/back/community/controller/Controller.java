package com.lsk.community.back.community.controller;

import com.lsk.community.back.common.authz.aspect.annotation.RequireLogin;
import com.lsk.community.back.common.authz.aspect.annotation.RequireRequestKey;
import com.lsk.community.back.common.response.aspect.annotation.DebugAPI;
import com.lsk.community.back.common.response.aspect.annotation.JsonReturn;
import com.lsk.community.back.community.work.WorkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired
	private WorkManager workManager;

	@JsonReturn
	@GetMapping("/queryAllWork")
	public Object queryAllWork(Integer page) {
		return workManager.queryAllWork(page);
	}

	@JsonReturn
	@DebugAPI
	@GetMapping("/updateWorkStatistic")
	public Object updateWorkStatistic(Integer visible, Integer invisible) {
		return workManager.updateWorkStatistic(visible, invisible, new WorkManager.WorkStatistic());
	}

	@JsonReturn
	@RequireLogin
	@RequireRequestKey
	@PostMapping("/createWork")
	public Object createWork(String name, @CookieValue("reqKey") String requestKey, @CookieValue("token") String token) {
		workManager.createWork(name, token);
		return "Success";
	}
}
