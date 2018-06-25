package com.dark.data.service;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import com.dark.data.domain.Admin;
import com.dark.data.persistence.ActivityTraceMapper;
import com.dark.data.persistence.AdminMapper;

@Service
public class AdminService {
	@Resource
	private AdminMapper adminMapper;
	
	public Admin checkPwd(String username, String password) {
		Admin admin=adminMapper.checkPwd(username,password);
		if(admin!=null){
			return admin;
		}
		return null;
	}

}
