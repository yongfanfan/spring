package com.dark.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dark.data.domain.Activity;
import com.dark.data.domain.AdminMapping;
import com.dark.data.persistence.ActivityMapper;
import com.dark.data.persistence.AdminMappingMapper;

@Service
public class AdminMappingService {
	@Resource
	private AdminMappingMapper adminMappingMapper;
	@Resource
	private ActivityMapper activityMapper;

	public List<Activity> selectActivityId(Long id) {
		List<Map<String,Object>> activityID= adminMappingMapper.selectActivityId(id);
		if(activityID.size()>0){
			List<Activity> list=new ArrayList<Activity>();
			for(Map<String,Object> map:activityID){
				System.out.println("activityid:"+map.get("activityId"));
				String title=activityMapper.selectByActId((Long)map.get("activityId"));
				Activity ac=new Activity();
				ac.setTitle(title);
				ac.setId((Long) map.get("activityId"));
				list.add(ac);
			}
			return list;
			
		}
		return null;
	}

}
