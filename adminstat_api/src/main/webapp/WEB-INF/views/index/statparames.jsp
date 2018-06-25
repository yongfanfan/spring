<!DOCTYPE html>
<%@ page language="java" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>后台管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/Css/bootstrap-responsive.css" />
    <link href="${ctx }/static/assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
    <link href="${ctx }/static/assets/css/bui-min.css" rel="stylesheet" type="text/css" />
    <link href="${ctx }/static/assets/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/Css/style.css" />
     <script type="text/javascript" src="${ctx }/static/Js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="${ctx }/static/Js/jquery.js"></script>
    <script type="text/javascript" src="${ctx }/static/Js/jquery.sorted.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }


    </style>
</head>
<body>

<div class="header">

    <div class="dl-title">
        云彩服务：<c:forEach items="${listActivity }" var="a">
          <c:if test='${activityId==a.id }'>
              <span>${a.title }</span> 
           </c:if>
       </c:forEach>活动统计 
    </div>
   
    <div class="dl-log">欢迎您，<span class="dl-log-user">${admin.name }</span><a href="${ctx }/logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>
<div class="content">


<form class="form-inline definewidth m20" action="${ctx }/checkparame" method="post"> 
  <select name="activityId" id="chckact">
       <option selected value="-1">请选择一个活动查看</option>
       <c:forEach items="${listActivity }" var="a">
          <option value="${a.id }" <c:if test='${activityId==a.id }'>selected</c:if> >${a.title }</option> 
       </c:forEach>
	</select>  
    <!--  <select name="checkparames">
       <option selected value="kong">请选择查询条件</option>
       <option value="phone">请输入电话号码</option> 
       <option value="network">请输入运营商</option> 
       <option value="status">请输入领取状态</option> 
	</select>  --> 
	<select name="network">
       <option selected value="kong">请选择运营商</option>
       <option value="中国移动">中国移动</option> 
       <option value="中国联通">中国联通</option> 
       <option value="中国电信">中国电信</option> 
	</select> 
	<select name="checkstatus">
       <option selected value="kong">请选择领取状态</option>
       <option value="SUCCESS">成功</option> 
       <option value="WAIT">失败</option> 
	</select> 
	请输入电话号码：
    <input type="text" name="rolename" id="rolename"class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <input type="text" name="activityId" value="${activityId }" style="display:none">
    <input type="text" name="pageNo" value="1" style="display:none">
    <button type="submit" class="btn btn-primary">查询</button>&nbsp;&nbsp;<!--  <button type="button" class="btn btn-success" id="addnew">新增机构</button> -->
</form>
<table class="table table-bordered table-hover definewidth m10" >
    <thead>
    <tr>
        <th>时间日期</th>
         <th>微信ID</th>
         <th>动作</th>
        <th>奖类</th>
        <th>运营商</th>
        <th>手机号</th>
        <th>领取数量</th>
        <th>领取状态</th>
    </tr>
    
    </thead>
    <c:forEach items="${viewparames}" var="listso">
          <tr>
             <td>${listso.time } </td>
              <td>${listso.openids }</td>
              <td>分享<c:if test="${empty listso.shares }">0</c:if><c:if test="${! empty listso.shares }">${listso.shares }</c:if>次</td>
              <td>流量</td>
             <td>${listso.network }</td> 
            <td>${listso.phone }</td>
            <td><c:if test="${empty listso.network }">0</c:if>
            <c:if test="${! empty listso.network }">
               <c:if test="${listso.network=='中国联通' }">20</c:if>
                <c:if test="${listso.network!='中国联通' }">10</c:if>
            </c:if></td> 
            <td><c:if test="${listso.status=='WAIT'}">失败</c:if><c:if test="${listso.status!='WAIT'}">成功</c:if></td> 
        </tr>  
       </c:forEach> 
        </table>
        <c:if test="${pageTotal>1}">
         <div class="pagination pull-right page">
         <ul>
          <li><a href="${ctx}/checkparame?pageNo=1&activityId=${activityId}&checkstatus=${checkstatus}&network=${network }&rolename=${rolename}">首页</a></li>
          <li><a href="${ctx}/checkparame?pageNo=${pageProv }&activityId=${activityId}&checkstatus=${checkstatus}&network=${network }&rolename=${rolename}">上一页</a></li>
          <li><a href="${ctx}/checkparame?pageNo=${pageNext }&activityId=${activityId}&checkstatus=${checkstatus}&network=${network }&rolename=${rolename}">下一页</a></li>
          <li><a href="${ctx}/checkparame?pageNo=${pageTotal }&activityId=${activityId}&checkstatus=${checkstatus}&network=${network }&rolename=${rolename}">尾页</a></li>
          <li><a>当前页：${pageCurr }</a></li>
          <li><a>总页数：${pageTotal }</a></li>
          <li><a>总条数：${totalCount }</a></li>
          </ul>
            </div>
            </c:if>
</div>
<div style="text-align:center;">
</div>
<script type="text/javascript">
   $(function(){
	   $("#chckact").on("change",function(){
		   var acid=$("#chckact").val();
		   window.location.href="${ctx}/index?pageNo=1&activityId="+acid+"";
	   });
   })
</script>
</body>
</html>