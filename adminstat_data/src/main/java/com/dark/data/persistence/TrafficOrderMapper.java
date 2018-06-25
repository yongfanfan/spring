package com.dark.data.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.dark.data.annotation.MyBatisRepository;
import com.dark.data.domain.TrafficOrder;

@MyBatisRepository
public interface TrafficOrderMapper {
	int deleteByPrimaryKey(Long id);

	int insert(TrafficOrder record);

	int insertSelective(TrafficOrder record);

	TrafficOrder selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TrafficOrder record);

	int updateByPrimaryKey(TrafficOrder record);

	@Select("select * from `traffic_order` where user_id=#{userId} and activity_id=#{activityId}")
	TrafficOrder selectByUserId(@Param("userId") Long userId,@Param("activityId") Long activityId);

	@Select("select case when count(1)>0 then 1 else 0 end as total from `traffic_order` where phone=#{phone} and activity_id=#{activityId}")
	@ResultType(Boolean.class)
	boolean checkPhone(@Param("phone") String phone,@Param("activityId") Long activityId);
	
	@ResultType(Integer.class)
	@Select("select count(*) from `traffic_order` where activity_id=#{id} and phone is not null")
	int selectRecevi(@Param("id") Long id);
	@ResultType(Integer.class)
	@Select("select count(*) from `traffic_order` where activity_id=#{id} and status='WAIT'")
	int selectNojoins(@Param("id") Long id);
    @Select("select count(*) from `traffic_order` where activity_id=#{activityId}")
    @ResultType(Integer.class)
	int selectcountAll(@Param("activityId")Long activityId);
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id,case when torder.activity_id=#{activityId} then 0 else 1 end as flag,"+
"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
"(select `openid` from `user` where id=torder.user_id) 'openids',"+
"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
"FROM `traffic_order` torder "+
"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
"GROUP BY flag,torder.user_id LIMIT #{items},#{pageSize}")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewOrder(@Param("activityId")Long activityId,@Param("items")int items, @Param("pageSize")int pageSize);
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id, "+
"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
"(select `openid` from `user` where id=torder.user_id) 'openids',"+
"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
"FROM `traffic_order` torder "+
"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
			"where torder.activity_id=#{activityId} and torder.phone=#{rolename} "+
			"GROUP BY torder.user_id ")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckPhone(@Param("activityId")Long activityId,@Param("rolename")String rolename);
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id,case when torder.activity_id=#{activityId} then 0 else 1 end as flag,"+
"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
"(select `openid` from `user` where id=torder.user_id) 'openids',"+
"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
"FROM `traffic_order` torder "+
"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
			"where torder.network=#{rolename} "+
			"GROUP BY flag,torder.user_id LIMIT #{items},#{pageSize}")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckNetwork(@Param("activityId")Long activityId,@Param("rolename")String rolename,@Param("items")int items, @Param("pageSize")int pageSize);
	@ResultType(Integer.class)
    @Select("select count(*) from `traffic_order` where network=#{rolename} and activity_id=#{activityId}")
	int selectNetworkAll(@Param("activityId")Long activityId,@Param("rolename")String rolename);
	@ResultType(Integer.class)
    @Select("select count(*) from `traffic_order` where status=#{rolename} and activity_id=#{activityId}")
	int selectStatusAll(@Param("activityId")Long activityId,@Param("rolename")String rolename);
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id,case when torder.activity_id=#{activityId} then 0 else 1 end as flag,"+
"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
"(select `openid` from `user` where id=torder.user_id) 'openids',"+
"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
"FROM `traffic_order` torder "+
"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
			"where torder.status=#{rolename} "+
			"GROUP BY flag,torder.user_id LIMIT #{items},#{pageSize}")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckStatus(@Param("activityId")Long activityId,@Param("rolename")String rolename,@Param("items")int items, @Param("pageSize")int pageSize);

	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id, "+
			"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
			"(select `openid` from `user` where id=torder.user_id) 'openids',"+
			"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
			"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
			"FROM `traffic_order` torder "+
			"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
						"where torder.activity_id=#{activityId} and torder.phone=#{rolename} and torder.network=#{network} "+
						"GROUP BY torder.user_id ")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckNetworkPhone(@Param("activityId")Long activityId,@Param("network")String network,@Param("rolename")String rolename);

	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id, "+
			"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
			"(select `openid` from `user` where id=torder.user_id) 'openids',"+
			"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
			"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
			"FROM `traffic_order` torder "+
			"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
						"where torder.activity_id=#{activityId} and torder.phone=#{rolename} and torder.network=#{network} and torder.status=#{checkstatus} "+
						"GROUP BY torder.user_id ")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckNetworkPhoneStatus(@Param("activityId")Long activityId,@Param("network")String network,@Param("rolename")String rolename, @Param("checkstatus")String checkstatus);
	
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id, "+
			"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
			"(select `openid` from `user` where id=torder.user_id) 'openids',"+
			"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
			"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
			"FROM `traffic_order` torder "+
			"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
						"where torder.activity_id=#{activityId} and torder.phone=#{rolename} and torder.status=#{checkstatus} "+
						"GROUP BY torder.user_id ")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckPhoneStatus(@Param("activityId")Long activityId,@Param("rolename")String rolename, @Param("checkstatus")String checkstatus);
	@ResultType(Integer.class)
    @Select("select count(*) from `traffic_order` where status=#{checkstatus} and activity_id=#{activityId} and network=#{network}")
	int selectNetworkStatusAll(@Param("activityId")Long activityId,@Param("network")String network, @Param("checkstatus")String checkstatus);
	@Select("SELECT torder.user_id,torder.`status`,torder.phone,torder.province,torder.network,torder.postpackage,DATE_FORMAT(torder.create_time,'%Y-%m-%d %H:%i:%s') 'time',torder.activity_id, "+
			"(select count(*) from traffic_order tor where tor.phone is not NULL  and tor.user_id=torder.user_id and tor.activity_id=torder.activity_id) 'lingqu', "+
			"(select `openid` from `user` where id=torder.user_id) 'openids',"+
			"(select `shares` from `activity_trace` atr where atr.user_id=torder.user_id and atr.activity_id=torder.activity_id) 'shares', "+
			"(select `title` from `activity` where id=torder.activity_id) 'activityTitle' "+
			"FROM `traffic_order` torder "+
			"left JOIN `activity_options` ao on ao.activity_id=torder.activity_id "+
						"where torder.activity_id=#{activityId} and torder.network=#{network} and torder.status=#{checkstatus} "+
						"GROUP BY torder.user_id LIMIT #{items},#{pageSize}")
	@ResultType(HashMap.class)
	List<Map<String, Object>> viewCheckNetworkStatus(@Param("activityId")Long activityId,@Param("network")String network,@Param("checkstatus")String checkstatus,@Param("items")int items, @Param("pageSize")int pageSize);


	
}