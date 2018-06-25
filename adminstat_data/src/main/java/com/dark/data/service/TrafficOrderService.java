package com.dark.data.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dark.data.domain.ActivityHistory;
import com.dark.data.domain.ItemType;
import com.dark.data.domain.SourceType;
import com.dark.data.domain.TrafficOrder;
import com.dark.data.persistence.ActivityHistoryMapper;
import com.dark.data.persistence.TrafficOrderMapper;

@Service
public class TrafficOrderService {
	@Resource
	private TrafficOrderMapper orderMapper;
	@Resource
	private ActivityHistoryMapper activityHistoryMapper;

	public void insert(TrafficOrder order,Long activityId) {
		Date now = new Date();
		order.setCreateTime(now);
		order.setActivityId(activityId);
		orderMapper.insertSelective(order);
		ActivityHistory history = new ActivityHistory();
		history.setUserId(order.getUserId());
		history.setActivityId(activityId);
		history.setSource(SourceType.JOIN.name());
		history.setItemType(ItemType.TRAFFIC.name());
		history.setOutId(order.getId());
		history.setCreateTime(now);
		activityHistoryMapper.insertSelective(history);
	}

	public TrafficOrder selectByUserId(Long userId,Long activityId) {
		return orderMapper.selectByUserId(userId,activityId);
	}

	public void update(TrafficOrder order) {
		orderMapper.updateByPrimaryKeySelective(order);
	}

	public TrafficOrder selectById(Long id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	public boolean checkPhone(String phone,Long activityId) {
		return orderMapper.checkPhone(phone,activityId);
	}

	public int selectcountAll(Long activityId) {
		int lists=orderMapper.selectcountAll(activityId);
		return lists;

	}

	public List<Map<String, Object>> viewOrder(Long activityId, int items, int pageSize) {
		return orderMapper.viewOrder(activityId,items,pageSize);
	}

	public List<Map<String, Object>> viewCheckPhone(Long activityid,String rolename) {
		return orderMapper.viewCheckPhone(activityid,rolename);
	}

	public List<Map<String, Object>> viewCheckNetwork(Long activityId,String rolename, int items, int pageSize) {
		return orderMapper.viewCheckNetwork(activityId,rolename,items,pageSize);
	}

	public int selectNetworkAll(Long activityId, String rolename) {
		return orderMapper.selectNetworkAll(activityId,rolename);
	}

	public int selectStatusAll(Long activityId, String rolename) {
		return orderMapper.selectStatusAll(activityId,rolename);
	}

	public List<Map<String, Object>> viewCheckStatus(Long activityId,String rolename, int items, int pageSize) {
		return orderMapper.viewCheckStatus(activityId,rolename,items,pageSize);
	}

	public List<Map<String, Object>> viewAdd(Long activityId, int items, int pageSize) {
		return orderMapper.viewOrder(activityId,items,pageSize);
	}

	public List<Map<String, Object>> viewCheckNetworkPhone(Long activityId,String network, String rolename) {
		return orderMapper.viewCheckNetworkPhone(activityId,network,rolename);
	}

	public List<Map<String, Object>> viewCheckNetworkPhoneStatus(Long activityId, String network, String rolename, String checkstatus) {
		return orderMapper.viewCheckNetworkPhoneStatus(activityId,network,rolename,checkstatus);
	}

	public List<Map<String, Object>> viewCheckPhoneStatus(Long activityId,String rolename, String checkstatus) {
		return orderMapper.viewCheckPhoneStatus(activityId,rolename,checkstatus);
	}

	public int selectNetworkStatusAll(Long activityId,String network, String checkstatus) {
		return orderMapper.selectNetworkStatusAll(activityId,network,checkstatus);
	}

	public List<Map<String, Object>> viewCheckNetworkStatus(Long activityId,String network, String checkstatus, int items, int pageSize) {
		return orderMapper.viewCheckNetworkStatus(activityId,network,checkstatus,items,pageSize);
	}
}
