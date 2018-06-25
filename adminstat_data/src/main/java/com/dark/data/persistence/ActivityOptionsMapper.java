package com.dark.data.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.ActivityOptions;

@MyBatisRepository
public interface ActivityOptionsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActivityOptions record);

    int insertSelective(ActivityOptions record);

    ActivityOptions selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityOptions record);

    int updateByPrimaryKey(ActivityOptions record);
    
    @Select("select id,item_type,quantity,content from activity_options where activity_id=#{activityId}")
    @ResultMap("BaseResultMap")
    List<ActivityOptions> selectOptionsById(@Param("activityId") long activityId);
    
    @Select("select id,item_type,quantity,content from activity_options where activity_id=(select id from activity where external_key=#{externalKey})")
    @ResultMap("BaseResultMap")
    List<ActivityOptions> selectOptionsByKey(@Param("externalKey") String externalKey);
    
    @Update("update activity_options set quantity=quantity-1 where id=#{id}")
	int updateQuantityById(@Param("id") Long id);
    @Select("select item_type from activity_options where activity_id=#{id} group by item_type")
    @ResultMap("BaseResultMap")
	List<ActivityOptions> selectOptionsType(@Param("id")Long id);
}