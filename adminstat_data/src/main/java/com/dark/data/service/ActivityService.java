package com.dark.data.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dark.data.domain.Activity;
import com.dark.data.domain.ActivityOptions;
import com.dark.data.domain.ActivityTrace;
import com.dark.data.domain.SourceType;
import com.dark.data.persistence.ActivityHistoryMapper;
import com.dark.data.persistence.ActivityMapper;
import com.dark.data.persistence.ActivityOptionsMapper;
import com.dark.data.persistence.ActivityTraceMapper;
import com.dark.data.persistence.TrafficOrderMapper;

@Service
public class ActivityService {
	@Resource
	private ActivityMapper activityMapper;
	@Resource
	private ActivityHistoryMapper activityHistoryMapper;
	@Resource
	private ActivityTraceMapper activityTraceMapper;
	@Resource
	private ActivityOptionsMapper activityOptionsMapper;
	@Resource
	private TrafficOrderMapper trafficOrderMapper;

	public void insertSelective(Activity record) {
		record.setCreateTime(new Date());
		activityMapper.insertSelective(record);
	}

	public void update(Activity record) {
		activityMapper.updateByPrimaryKeySelective(record);
	}
	
	public ActivityTrace selectTraceByPrimaryKey(Long activityId,Long userId) {
		return activityTraceMapper.selectByPrimaryKey(activityId,userId);
	}
	
	public Long selectIdByExternalKey(String externalKey) {
		return activityMapper.selectIdByExternalKey(externalKey);
	}
	
	public boolean isShared(Long userId,Long activityId) {
		Integer count = activityMapper.selectCountByUidAidType(userId,activityId,SourceType.SHARE.name());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isJoined(Long userId,Long activityId) {
		Integer count = activityMapper.selectCountByUidAidType(userId,activityId,SourceType.JOIN.name());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void insertOrUpateTraceShareTime(boolean isInsert,Long userId,Long activityId) {
		if (!isInsert) {
			activityTraceMapper.updateShareTime(activityId, userId);
		} else {
			ActivityTrace trace = new ActivityTrace();
			trace.setActivityId(activityId);
			trace.setUserId(userId);
			trace.setShareTime(new Date());
			trace.setShares(1);
			activityTraceMapper.insertSelective(trace);
		}
	}
	
	public void insertOrUpateTraceJoinTime(boolean isInsert,Long userId,Long activityId) {
		//异步更新
		if (!isInsert) {
			activityTraceMapper.updateJoinTime(activityId, userId);
		} else {
			ActivityTrace trace = new ActivityTrace();
			trace.setActivityId(activityId);
			trace.setUserId(userId);
			trace.setJoinTime(new Date());
			trace.setJoins(1);
			activityTraceMapper.insertSelective(trace);
		}
	}

	public List<Activity> selectAll() {
		List<Activity> lists=activityMapper.selectAll();
		List<Activity> list=new ArrayList<Activity>();
		for(Activity i : lists){
			Activity a=new Activity();
			int joins=activityTraceMapper.selectJoins(i.getId());
			a.setTitle(i.getTitle());
			a.setId(i.getId());
			a.setJoins(joins);
			if(joins>0){
				int recevi=trafficOrderMapper.selectRecevi(i.getId());
				int shares=activityTraceMapper.selectShares(i.getId());
				int nojoins=trafficOrderMapper.selectNojoins(i.getId());
				a.setReceives(recevi);
				a.setNonejoins(nojoins);
				a.setShares(shares);
			}
			list.add(a);
		}
		return list;
	}

	public List<Activity> selectHistoryAll() {
		List<Activity> lists=activityMapper.selectcaipiapAll();
		List<Activity> list=new ArrayList<Activity>();
		for(Activity i : lists){
			System.out.println("title:"+i.getTitle()+",id:"+i.getId());
			Activity a=new Activity();
			int totalsend=activityHistoryMapper.selectTotalSend(i.getId());
			int totalqiang=activityHistoryMapper.selectTotalQiang(i.getId());
			int totalshare=activityHistoryMapper.selectTotalShare(i.getId());
			a.setTotal_qiang(totalqiang);
			a.setTotal_send(totalsend);
			a.setTotal_share(totalshare);
			a.setId(i.getId());
			Map<String, Object> map=activityMapper.selecttitleAll(i.getId());
			if(!map.isEmpty()){
				a.setTitle((String) map.get("title"));
//				a.setTicket_rest((int) map.get("ticket_rest"));
//				a.setTicket_total((int) map.get("ticket_total"));
			}
			list.add(a);
		}
		return list;
	}

	public List<Map<String, Object>> selectHistorysoAll(Long id) {
		List<Map<String, Object>> map=activityHistoryMapper.selecttime(id);
		if(map.size()>0){
			return map;
		}
		return null;
	}

	public Activity selectByExternalKey(String rid) {
		return activityMapper.selectByExternalKey(rid);
	}

	public List<ActivityOptions> selectOptions(Long id) {
		return activityOptionsMapper.selectOptionsById(id);
	}

	public List<ActivityOptions> selectOptionsType(Long id) {
		return activityOptionsMapper.selectOptionsType(id);
	}
	
}
