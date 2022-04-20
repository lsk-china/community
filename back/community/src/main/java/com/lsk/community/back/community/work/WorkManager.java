package com.lsk.community.back.community.work;

import com.google.gson.Gson;
import com.lsk.community.back.common.redis.RedisComponent;
import com.lsk.community.back.community.client.AuthClient;
import com.lsk.community.back.community.mapper.WorkMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WorkManager {
	private static final Integer pageSize = 10;
	@Autowired
	private RedisComponent redis;
	@Autowired
	private WorkMapper workMapper;
	@Autowired
	private AuthClient authClient;
	@Autowired
	private Gson gson;

	public WorkStatistic updateWorkStatistic(int diffVisible, int diffInvisible, WorkStatistic oldValue) {
		oldValue.setVisible(oldValue.getVisible() + diffVisible);
		oldValue.setInvisible(oldValue.getInvisible() + diffInvisible);
		oldValue.calcTotal();
		redis.set("WORK-STATISTIC", oldValue);
		return oldValue;
	}

	public WorkStatistic getWorkStatistic() {
		WorkStatistic workStatistic = null;
		if (!redis.has("WORK-STATISTIC")) {
			Integer visibleCount = workMapper.visibleWorkCount();
			Integer invisibleCount = workMapper.visibleWorkCount();
			workStatistic = updateWorkStatistic(visibleCount, invisibleCount, new WorkStatistic());
		} else {
			workStatistic = redis.get("WORK-STATISTIC", WorkStatistic.class);
		}
		return workStatistic;
	}

	public Map<String, Object> queryAllWork(Integer page) {
		WorkStatistic workStatistic = getWorkStatistic();
		Map<String, Object> result = new HashMap<>();
		result.put("currentPage", page);
		result.put("pageSize", pageSize);
		result.put("totalPages", Math.ceil(workStatistic.getTotal() / pageSize));
		result.put("result", workMapper.queryAllVisibleWorks((page - 1) * pageSize, pageSize));
		return result;
	}

	public void createWork(String name, String token) {
		Map<String, Object> userinfo = gson.fromJson(authClient.userinfo(token), Map.class);
		Integer uid = (Integer) userinfo.get("uid");
		workMapper.createWork(name, uid);
	}

	@Data
	public static class WorkStatistic {
		private Integer total;
		private Integer visible;
		private Integer invisible;

		public void calcTotal() {
			this.total = this.visible + this.invisible;
		}

		public WorkStatistic(){
			this.total = 0;
			this.visible = 0;
			this.invisible = 0;
		}
	}
}
