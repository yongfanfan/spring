package com.dark.data.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.Activity;

@MyBatisRepository
public interface ActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKeyWithBLOBs(Activity record);

    int updateByPrimaryKey(Activity record);
    
    @Select("select id from activity where external_key=#{externalKey}")
    @ResultType(Long.class)
    Long selectIdByExternalKey(@Param("externalKey")String externalKey);

    @Select("SELECT count(*) FROM activity_history where user_id=#{userId} and activity_id=#{activityId} and source=#{type}")
    @ResultType(Integer.class)
	Integer selectCountByUidAidType(@Param("userId")Long userId, @Param("activityId")Long activityId, @Param("type")String type);
    @ResultMap("BaseResultMap")
    @Select("select * from `xdbpp`.`activity` order by create_time desc")
	List<Activity> selectAll();
    
    @Select("select title,ticket_total,ticket_rest from `lottery`.`activity` where id=#{id}")
    @ResultType(HashMap.class)
    Map<String, Object> selecttitleAll(Long id);
    @ResultMap("BaseResultMap")
    @Select("select * from `lottery`.`activity` order by create_time desc")
	List<Activity> selectcaipiapAll();
    @Select("select title from `xdbpp`.`activity` where id=#{actid}")
    @ResultType(String.class)
	String selectByActId(Long actid);
    @ResultMap("BaseResultMap")
    @Select("select * from activity where external_key=#{rid}")
	Activity selectByExternalKey(String rid);
    
}