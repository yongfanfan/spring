package com.dark.data.persistence;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.User;

@MyBatisRepository
public interface UserMapper {
	int deleteByPrimaryKey(Long id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	@Select("select * from user where openid=#{openid}")
	User selectByOpenid(@Param("openid") String openid);

	@Select("select shared from user where openid=#{openid}")
	@ResultType(Boolean.class)
	boolean checkShared(@Param("openid") String openid);

	@Update("update user set shared=1 where openid=#{openid}")
	@ResultType(Integer.class)
	int updateShared(@Param("openid") String openid);

	@Select("select id from user where openid=#{openid}")
	@ResultType(Long.class)
	Long selectIdByOpenid(@Param("openid") String openid);
}