package com.dark.data.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.dark.common.domain.Pager;
import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.ActivityHistory;

@MyBatisRepository
public interface ActivityHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActivityHistory record);

    int insertSelective(ActivityHistory record);

    ActivityHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityHistory record);

    int updateByPrimaryKey(ActivityHistory record);
    @ResultMap("BaseResultMap")
    @Select("select * from activity_history")
	List<ActivityHistory> selectHistoryAll();
    @Select("select count(*) from `lottery`.`activity_history`  where activity_id=#{id}")
    @ResultType(Integer.class)
	int selectTotalSend(Long id);
    @Select("select count(*) from `lottery`.`activity_history`  where activity_id=#{id} and source='QIANG'")
    @ResultType(Integer.class)
	int selectTotalQiang(Long id);
    @Select("select count(*) from `lottery`.`activity_history`  where activity_id=#{id} and source='SHARE'")
    @ResultType(Integer.class)
	int selectTotalShare(Long id);
    @Select("SELECT DATE_FORMAT(t.create_time,'%Y%m%d') time,count(t.id) 'send',"+
"(select count(*) from `lottery`.`activity_history` where activity_id=#{id} and source='SHARE' and DATE_FORMAT(create_time,'%Y%m%d')=time ) as 'share',"+
"(select count(*) from `lottery`.`activity_history` where activity_id=#{id} and source='QIANG' and DATE_FORMAT(create_time,'%Y%m%d')=time ) as 'join' "+
"FROM `lottery`.`activity_history` t where t.activity_id=#{id}"+
" group by time")
    @ResultType(HashMap.class)
	List<Map<String, Object>> selecttime(Long id);

	List<Map<String, Object>> selectAll(@Param("rid")String rid,@Param("phone")String phone, @Param("username")String username, @Param("status")String status, @Param("pager")Pager pager);

	List<Map<String, Object>> selectByTraff(@Param("activityId")Long activityId,@Param("phone")String phone, @Param("username")String username, @Param("status")String status, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);

	List<Map<String, Object>> selectByEntity(@Param("activityId")Long activityId,@Param("phone")String phone, @Param("username")String username, @Param("status")String status, @Param("pageNo")int pageNo, @Param("pageSize")int pageSize);
	
	Integer selectByTraffcount(@Param("id")Long id,@Param("phone")String phone, @Param("username")String username, @Param("status")String status);
	
	Integer selectByEntitycount(@Param("id")Long id, @Param("phone")String phone, @Param("username")String username, @Param("status")String status);
}