package com.dark.data.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dark.data.domain.User;
import com.dark.data.persistence.UserMapper;

@Service
public class UserService {
	@Resource
	private UserMapper userMapper;

	public void insert(User user) {
		user.setCreateTime(new Date());
		userMapper.insertSelective(user);
	}

	public User selectByOpenid(String openid) {
		return userMapper.selectByOpenid(openid);
	}
	
	public Long selectIdByOpenid(String openid) {
		return userMapper.selectIdByOpenid(openid);
	}

	public void update(User user) {
		userMapper.updateByPrimaryKeySelective(user);
	}
}
