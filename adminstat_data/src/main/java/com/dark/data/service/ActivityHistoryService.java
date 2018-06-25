package com.dark.data.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dark.common.domain.Pager;
import com.dark.data.persistence.ActivityHistoryMapper;
import com.dark.data.persistence.ActivityMapper;

@Service
public class ActivityHistoryService {
	@Resource
	private ActivityHistoryMapper activityHistoryMapper;

	public List<Map<String, Object>> selectAll(String rid, String phone, String username, String status, Pager pager) {
		return activityHistoryMapper.selectAll(rid,phone,username,status,pager);
	}

	public List<Map<String, Object>> selectByTraff(Long activityId, String phone, String username, String status,int pagerNo,int pageSize) {
		return activityHistoryMapper.selectByTraff(activityId,phone,username,status,pagerNo,pageSize);
	}

	public List<Map<String, Object>> selectByEntity(Long activityId, String phone, String username, String status,int pagerNo,int pageSize) {
		return activityHistoryMapper.selectByEntity(activityId,phone,username,status,pagerNo,pageSize);
	}

	public int selectByTraffcount(Long id,String phone, String username, String status) {
		return activityHistoryMapper.selectByTraffcount(id,phone,username,status)==null?0:activityHistoryMapper.selectByTraffcount(id,phone,username,status);
	}

	public int selectByEntitycount(Long id, String phone, String username, String status) {
		return activityHistoryMapper.selectByEntitycount(id,phone,username,status)==null?0:activityHistoryMapper.selectByEntitycount(id,phone,username,status);
	}
}
