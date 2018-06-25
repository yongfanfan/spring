package com.dark.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dark.api.base.Response;
import com.dark.common.domain.Pager;
import com.dark.data.domain.Activity;
import com.dark.data.domain.ActivityOptions;
import com.dark.data.domain.statTraffic;
import com.dark.data.service.ActivityHistoryService;
import com.dark.data.service.ActivityService;
import com.dark.data.service.AdminMappingService;

@Controller
@RequestMapping(value = "/serach")
public class SerachController {
	@Resource
	private AdminMappingService adminMappingService;
	@Resource
	private ActivityHistoryService activityHistoryService; 
	@Resource
	private ActivityService activityService; 
	
//	@RequestMapping(value = "/index/{rid}")
//	public ModelAndView index(@PathVariable(value = "rid") String rid,
//			@RequestParam(value = "type", required = false, defaultValue = "")String type,
//    		@RequestParam(value = "phone", required = false, defaultValue = "")String phone,
//    		@RequestParam(value = "username", required = false, defaultValue = "")String username,
//    		@RequestParam(value = "status", required = false, defaultValue = "")String status,
//			Pager pager) {
//		ModelAndView mv = new ModelAndView("5zxjai02mnlukdy2"+"/index");
//		Activity activity=activityService.selectByExternalKey(rid);
//		if(activity==null){
//			return mv;
//		}
//		List<ActivityOptions> activityOptions=activityService.selectOptionsType(activity.getId());
//		//List<Map<String, Object>> lists=activityHistoryService.selectAll(rid,phone,username,status,pager);
//		//List<Map<String, Object>> lists=activityHistoryService.selectByTraff(rid,phone,username,status,1,10);
//		return mv.addObject("type", type).addObject("activity", activity).addObject("activityOptions", activityOptions);
//	}
	
	@RequestMapping(value = "/selectByType/{rid}")
	public ModelAndView  selectByType(@PathVariable(value = "rid") String rid,
    		@RequestParam(value = "type", required = false, defaultValue = "TRAFFIC")String type,
    		@RequestParam(value = "phone", required = false, defaultValue = "")String phone,
    		@RequestParam(value = "username", required = false, defaultValue = "")String username,
    		@RequestParam(value = "status", required = false, defaultValue = "")String status,
    		@RequestParam(value = "pageNo", required = false, defaultValue = "1")String pageNo){
		List<Map<String, Object>> lists=null;
		ModelAndView mv = new ModelAndView("5zxjai02mnlukdy2"+"/index");
		Activity activity=activityService.selectByExternalKey(rid);
		if(activity==null){
			return mv;
		}
		int list=0;
		List<ActivityOptions> activityOptions=activityService.selectOptionsType(activity.getId());
		if("TRAFFIC".equals(type)){
			 list=activityHistoryService.selectByTraffcount(activity.getId(),phone,username,status);
		}else if("ENTITY".equals(type)){
			 list=activityHistoryService.selectByEntitycount(activity.getId(),phone,username,status);
		}
		System.out.println("llistsize;"+list);
		if(list>0){
			//
		   int totalCount =list;
		   int pageNoInteger=1;
		   int pageProv;
		   int pageNext;
		   int pageSize=15;
		   int pageTotal;
		   if("1".equals(pageNo)){
			   pageNoInteger=1;
		   }
		   pageNoInteger=Integer.parseInt(pageNo);
		   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
		   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
		   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
		   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
		   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
		   int items=pageSize*(pageNoInteger-1);
		   Long activityId=activityService.selectIdByExternalKey(rid);
		   if("TRAFFIC".equals(type)){
				lists=activityHistoryService.selectByTraff(activityId,phone,username,status,items,pageSize);
			}else if("ENTITY".equals(type)){
				lists=activityHistoryService.selectByEntity(activityId,phone,username,status,items,pageSize);
			}
			//
		   mv.addObject("totalCount", totalCount);
			mv.addObject("pageProv", pageProv);
			mv.addObject("pageNext", pageNext);
			mv.addObject("pageTotal", pageTotal);
			mv.addObject("pageCurr", pageNoInteger).addObject("phone", phone).addObject("username", username).addObject("status", status);
			mv.addObject("lists", lists);
		}
		return mv.addObject("type", type).addObject("activity", activity).addObject("activityOptions", activityOptions);
	}
	
}
