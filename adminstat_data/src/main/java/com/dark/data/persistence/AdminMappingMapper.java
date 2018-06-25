package com.dark.data.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.Admin;
import com.dark.data.domain.AdminMapping;

@MyBatisRepository
public interface AdminMappingMapper {
	@Select("select activity_id 'activityId' from `xdbpp`.`admin_mapping` where admin_id=#{id}")
    @ResultType(HashMap.class)
	List<Map<String,Object>> selectActivityId(Long id);
}
