package com.dark.data.persistence;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.ActivityTrace;

@MyBatisRepository
public interface ActivityTraceMapper {
    int deleteByPrimaryKey(@Param("activityId") Long activityId, @Param("userId") Long userId);

    int insert(ActivityTrace record);

    int insertSelective(ActivityTrace record);

    ActivityTrace selectByPrimaryKey(@Param("activityId") Long activityId, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(ActivityTrace record);

    int updateByPrimaryKey(ActivityTrace record);
    
    @Update("update activity_trace set last_join_time=join_time,join_time=CURRENT_TIMESTAMP(),joins=joins+1 where user_id=#{userId} and activity_id=#{activityId}")
    void updateJoinTime(@Param("activityId") Long activityId, @Param("userId") Long userId);
    
    @Update("update activity_trace set last_share_time=share_time,share_time=CURRENT_TIMESTAMP(),shares=shares+1 where user_id=#{userId} and activity_id=#{activityId}")
    void updateShareTime(@Param("activityId") Long activityId, @Param("userId") Long userId);

	@ResultType(Integer.class)
	@Select("select sum(shares) from `activity_trace` where activity_id=#{id}")
	int selectShares(@Param("id") Long id);
	@ResultType(Integer.class)
	@Select("select count(*) from `activity_trace` where activity_id=#{id}")
	int selectJoins(@Param("id") Long id);
}