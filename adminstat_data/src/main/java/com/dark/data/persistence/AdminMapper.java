package com.dark.data.persistence;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.Admin;

@MyBatisRepository
public interface AdminMapper {
	
	@Select("select * from `xdbpp`.`admin` where account=#{username} and password=#{password}")
    @ResultType(Admin.class)
	Admin checkPwd(@Param("username")String username, @Param("password")String password);

}
