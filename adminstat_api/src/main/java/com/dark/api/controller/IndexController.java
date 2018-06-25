package com.dark.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dark.api.base.Response;
import com.dark.api.util.WxTools;
import com.dark.data.domain.Activity;
import com.dark.data.domain.Admin;
import com.dark.data.domain.AdminMapping;
import com.dark.data.domain.TrafficOrder;
import com.dark.data.domain.statTraffic;
import com.dark.data.service.ActivityService;
import com.dark.data.service.AdminMappingService;
import com.dark.data.service.AdminService;
import com.dark.data.service.TrafficOrderService;
@Controller
public class IndexController {
	//private final static Long activityId=(long) 20003;
	@Resource
	private TrafficOrderService trafficOrderService;
	@Resource
	private AdminService adminService;
	@Resource
	private AdminMappingService adminMappingService;
	
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index/login");
		return mv;
	}
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request) {
		request.getSession().invalidate();
		ModelAndView mv = new ModelAndView("index/login");
		return mv;
	}
	@RequestMapping(value = "/checkLogin")
	public ModelAndView checkLogin(String username,String password,HttpServletRequest request) {
		String pwd=WxTools.md5(password);
		Admin admin = adminService.checkPwd(username,pwd);
	    if(admin!=null){
	    	List<Activity>  listActivity=adminMappingService.selectActivityId(admin.getId());
	    	System.out.println("true");
	    	HttpSession session = request.getSession();
			session.setAttribute("islogin", "true");
			session.setAttribute("admin", admin);
			session.setAttribute("listActivity", listActivity);
			ModelAndView mv = new ModelAndView("index/index");
			return mv;
	    }
	    System.out.println("false");
		ModelAndView mv = new ModelAndView("index/login");
		return mv;
	}
	
	@RequestMapping(value = "/checkparame")
	public ModelAndView checkparame(@RequestParam(value="pageNo",required=false, defaultValue = "")int pageNo,
			@RequestParam(value="checkstatus",required=false, defaultValue = "")String checkstatus,
			@RequestParam(value="network",required=false, defaultValue = "")String network,
			@RequestParam(value="rolename",required=false, defaultValue = "")String rolename,
			@RequestParam(value="activityId",required=false, defaultValue = "")Long activityId,HttpServletRequest request) {
		ModelAndView mv;
		if(network.equals("kong")&&StringUtils.isEmpty(rolename)&&checkstatus.equals("kong")){
			mv = index(1,activityId,request);
	    	return mv;
		}
		else{
			mv = new ModelAndView("index/statparames");
			if(!network.equals("kong")&&StringUtils.isEmpty(rolename)&&checkstatus.equals("kong")){
				int lists=trafficOrderService.selectNetworkAll(activityId,network);
				if(lists>0){
					pageNetwork(lists,pageNo,checkstatus,network,rolename, activityId, mv,request);
					return mv;
				}
				else{
					mv = index(1,activityId,request);
				}
			}else if(!network.equals("kong")&&!StringUtils.isEmpty(rolename)&&checkstatus.equals("kong")){
				pageNetworkRolename(pageNo,checkstatus,network,rolename, activityId, mv);
				return mv;
			}else if(!network.equals("kong")&&!StringUtils.isEmpty(rolename)&&!checkstatus.equals("kong")){
				pageNetworkRolStatus(pageNo,checkstatus,network,rolename, activityId, mv);
				return mv;
			}else if(!network.equals("kong")&&StringUtils.isEmpty(rolename)&&!checkstatus.equals("kong")){
				int lists=trafficOrderService.selectNetworkStatusAll(activityId,network,checkstatus);
				if(lists>0){
					pageNetworkStatus(lists,pageNo,checkstatus,network,rolename, activityId, mv,request);
					return mv;
				}
				else{
					mv = index(1,activityId,request);
				}
			}else if(network.equals("kong")&&!StringUtils.isEmpty(rolename)&&!checkstatus.equals("kong")){
				pageRolanmeStatus(pageNo,checkstatus,network,rolename, activityId, mv);
				return mv;
			}else if(network.equals("kong")&&StringUtils.isEmpty(rolename)&&!checkstatus.equals("kong")){
				if(checkstatus.equals("SUCCESS")){
					pageStatus(pageNo,checkstatus,network,rolename, activityId, mv);
					return mv;
				}else{
					int lists=trafficOrderService.selectStatusAll(activityId,"WAIT");
					if(lists>0){
						pageStatusSB(lists,pageNo,checkstatus,network,rolename, activityId, mv,request);
						return mv;
					}
					else{
						mv = index(1,activityId,request);
					}
				}
			}else if(network.equals("kong")&&!StringUtils.isEmpty(rolename)&&checkstatus.equals("kong")){
				pagePhone(pageNo,checkstatus,network,rolename, activityId, mv);
				return mv;
			}
		}
		mv = index(1,activityId,request);
    	return mv;
	}
	
	@RequestMapping(value = "/index")
	public ModelAndView index(int pageNo,Long activityId,HttpServletRequest request) {
		boolean isLogin = Boolean.parseBoolean((String)request.getSession().getAttribute("islogin"));
		if(isLogin){
			ModelAndView mv = new ModelAndView("index/index");
			pageView(pageNo,activityId,mv);
			return mv;
		}
		return new ModelAndView("index/login");
	}
	
	private void pageView(int pageNo,Long activityId,ModelAndView mv){
		int lists=trafficOrderService.selectcountAll(activityId);
		System.out.println("llistsize;"+lists);
		if(lists>0){
			//
		   int totalCount =lists;
		   if(activityId==20001){
			   totalCount=19756;
		   }else if(activityId==20005){
			   totalCount=8352;
		   }
		   int pageNoInteger=1;
		   int pageProv;
		   int pageNext;
		   int pageSize=15;
		   int pageTotal;
		   pageNoInteger=pageNo;
		   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
		   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
		   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
		   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
		   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
		   int items=pageSize*(pageNoInteger-1);
		   String titl="";
		   List<Map<String, Object>> viewOrder=trafficOrderService.viewOrder(activityId,items,pageSize);
		   List<statTraffic> lis=new ArrayList<statTraffic>();
		   for(Map<String, Object> s:viewOrder){
			   statTraffic st=new statTraffic();
			   titl=(String) s.get("activityTitle");
				   st.setTime((String) s.get("time"));
				   st.setOpenids((String) s.get("openids"));
				   st.setStatus((String) s.get("status"));
				   st.setItemtype("TRAFFIC");
				   st.setLingqu((Long) s.get("lingqu"));
				   st.setPhone((String) s.get("phone"));
				   st.setActivityTitle((String) s.get("activityTitle"));
				   st.setNetwork((String) s.get("network"));
				   st.setShares((Long) s.get("shares"));
			   lis.add(st);
		   }
		   
			//
		   mv.addObject("totalCount", totalCount);
		   mv.addObject("activityTitle", lis.get(0).getActivityTitle());
			mv.addObject("listsOrder", lis);
			mv.addObject("activityId", activityId);
			mv.addObject("pageProv", pageProv);
			mv.addObject("pageNext", pageNext);
			mv.addObject("pageTotal", pageTotal);
			mv.addObject("pageCurr", pageNoInteger);
		}
	}
	//成功
	private void pageStatus(int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv){
		String che="SUCCESS";
		 int totalCount =0;
		   if(activityId==20001){
			   totalCount=14012;
		   }else if(activityId==20005){
			   totalCount=6749;
		   }
		   int pageNoInteger=1;
		   int pageProv;
		   int pageNext;
		   int pageSize=15;
		   int pageTotal;
		   pageNoInteger=pageNo;
		   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
		   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
		   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
		   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
		   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
		   int items=pageSize*(pageNoInteger-1);
		   List<Map<String, Object>> viewparames=trafficOrderService.viewCheckStatus(activityId,che,items,pageSize);
		   List<statTraffic> lis=new ArrayList<statTraffic>();
		   for(Map<String, Object> s:viewparames){
			   statTraffic st=new statTraffic();
			   st.setActivityTitle((String) s.get("activityTitle"));
			   st.setNetwork((String) s.get("network"));
			   st.setShares((Long) s.get("shares"));
			   st.setItemtype("TRAFFIC");
			   st.setLingqu((Long) s.get("lingqu"));
			   st.setPhone((String) s.get("phone"));
			   st.setTime((String) s.get("time"));
			   st.setOpenids((String) s.get("openids"));
			   st.setStatus((String) s.get("status"));
			   lis.add(st);
		   }
		   mv.addObject("totalCount", totalCount);
		   mv.addObject("activityTitle", lis.get(0).getActivityTitle());
			mv.addObject("viewparames", lis);
			mv.addObject("activityId", activityId);
			mv.addObject("pageProv", pageProv);
			mv.addObject("pageNext", pageNext);
			mv.addObject("pageTotal", pageTotal);
			mv.addObject("pageCurr", pageNoInteger);
			mv.addObject("checkstatus", checkstatus);
			mv.addObject("network", network);
			mv.addObject("rolename", rolename);
			
	}
	//失败
	private void pageStatusSB(int lists,int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv,HttpServletRequest request){
		String che="WAIT";
		if(lists>0){
			//
		 int totalCount =lists;
		   int pageNoInteger=1;
		   int pageProv;
		   int pageNext;
		   int pageSize=15;
		   int pageTotal;
		   pageNoInteger=pageNo;
		   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
		   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
		   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
		   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
		   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
		   int items=pageSize*(pageNoInteger-1);
		   String titl="";
		   List<Map<String, Object>> viewparames=trafficOrderService.viewCheckStatus(activityId,che,items,pageSize);
		   List<statTraffic> lis=new ArrayList<statTraffic>();
		   for(Map<String, Object> s:viewparames){
			   statTraffic st=new statTraffic();
			   titl=(String) s.get("activityTitle");
			   st.setActivityTitle((String) s.get("activityTitle"));
			   st.setNetwork((String) s.get("network"));
			   st.setShares((Long) s.get("shares"));
			   st.setItemtype("TRAFFIC");
			   st.setLingqu((Long) s.get("lingqu"));
			   st.setPhone((String) s.get("phone"));
			   st.setTime((String) s.get("time"));
			   st.setOpenids((String) s.get("openids"));
			   st.setStatus((String) s.get("status"));
			   lis.add(st);
		   }
		   mv.addObject("totalCount", totalCount);
		   mv.addObject("activityTitle", lis.get(0).getActivityTitle());
			mv.addObject("viewparames", lis);
			mv.addObject("activityId", activityId);
			mv.addObject("pageProv", pageProv);
			mv.addObject("pageNext", pageNext);
			mv.addObject("pageTotal", pageTotal);
			mv.addObject("pageCurr", pageNoInteger);
			mv.addObject("checkstatus", checkstatus);
			mv.addObject("network", network);
			mv.addObject("rolename", rolename);
		}
	}
	//运营商
	private void pageNetwork(int lists,int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv,HttpServletRequest request){
	if(lists>0){
		//
	   int totalCount =lists;
	   int pageNoInteger=1;
	   int pageProv;
	   int pageNext;
	   int pageSize=15;
	   int pageTotal;
	   pageNoInteger=pageNo;
	   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
	   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
	   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
	   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
	   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
	   int items=pageSize*(pageNoInteger-1);
	   
	   List<Map<String, Object>> viewparames=trafficOrderService.viewCheckNetwork(activityId,network,items,pageSize);
	   mv.addObject("activityTitle", viewparames.get(0).get("activityTitle"));
	   mv.addObject("totalCount", totalCount);
	   mv.addObject("viewparames", viewparames);
		mv.addObject("activityId", activityId);
		mv.addObject("pageProv", pageProv);
		mv.addObject("pageNext", pageNext);
		mv.addObject("pageTotal", pageTotal);
		mv.addObject("pageCurr", pageNoInteger);
		mv.addObject("checkstatus", checkstatus);
		mv.addObject("network", network);
		mv.addObject("rolename", rolename);
	}
	}
	//电话号码
	private void pagePhone(int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv){
		List<Map<String, Object>> viewparames=trafficOrderService.viewCheckPhone(activityId,rolename);
		mv.addObject("viewparames", viewparames);
		mv.addObject("totalCount", viewparames.size());
		mv.addObject("activityId", activityId);
	}
	//net,phone
	private void pageNetworkRolename(int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv){
		List<Map<String, Object>> viewparames=trafficOrderService.viewCheckNetworkPhone(activityId,network,rolename);
		mv.addObject("viewparames", viewparames);
		mv.addObject("totalCount", viewparames.size());
		mv.addObject("activityId", activityId);
	}
	//net ,phone,status
	private void pageNetworkRolStatus(int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv){
		List<Map<String, Object>> viewparames=trafficOrderService.viewCheckNetworkPhoneStatus(activityId,network,rolename,checkstatus);
		mv.addObject("viewparames", viewparames);
		mv.addObject("totalCount", viewparames.size());
		mv.addObject("activityId", activityId);
	}
	//phone,status
	private void pageRolanmeStatus(int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv){
		System.out.println("network::::"+network);
		System.out.println("checkstatus::::"+checkstatus);
		List<Map<String, Object>> viewparames=trafficOrderService.viewCheckPhoneStatus(activityId,rolename,checkstatus);
		mv.addObject("viewparames", viewparames);
		mv.addObject("totalCount", viewparames.size());
		mv.addObject("activityId", activityId);
	}
    //net ,status
	private void pageNetworkStatus(int lists,int pageNo,String checkstatus,String network,String rolename,Long activityId,ModelAndView mv,HttpServletRequest request){
		System.out.println("panduan:::"+StringUtils.isEmpty(rolename));
		if(lists>0){
			//
		   int totalCount =lists;
		   int pageNoInteger=1;
		   int pageProv;
		   int pageNext;
		   int pageSize=15;
		   int pageTotal;
		   pageNoInteger=pageNo;
		   pageTotal=totalCount % pageSize ==0 ? totalCount / pageSize : totalCount / pageSize + 1;
		   pageNoInteger=pageNoInteger<=1?1:pageNoInteger;
		   pageNoInteger=pageNoInteger>=pageTotal?pageTotal:pageNoInteger;
		   pageProv=pageNoInteger<=1?1:pageNoInteger-1;
		   pageNext=pageNoInteger>=pageTotal?pageTotal:pageNoInteger+1;
		   int items=pageSize*(pageNoInteger-1);
		   
		   List<Map<String, Object>> viewparames=trafficOrderService.viewCheckNetworkStatus(activityId,network,checkstatus,items,pageSize);
		   mv.addObject("totalCount", totalCount);
		   mv.addObject("viewparames", viewparames);
			mv.addObject("activityId", activityId);
			mv.addObject("pageProv", pageProv);
			mv.addObject("pageNext", pageNext);
			mv.addObject("pageTotal", pageTotal);
			mv.addObject("pageCurr", pageNoInteger);
			mv.addObject("checkstatus", checkstatus);
			mv.addObject("network", network);
			mv.addObject("rolename", rolename);
		}
	}
}