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
        <!--<img src="/chinapost/Public/assets/img/top.png">-->
         云彩服务：${activity.title } 活动统计 
    </div>

 <%--    <div class="dl-log">  欢迎您，<span class="dl-log-user">${admin.name }</span><a href="${ctx }/logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div> --%>
</div>
<div class="content">

<form class="form-inline definewidth m20" action="${ctx }/serach/selectByType/${rid}" method="post">  
 <select name="type" id="type">
<!--  <option selected value="">请选择领取类型</option> -->
       <c:forEach items="${activityOptions }" var="a">
          <option value="${a.itemType }" <c:if test='${type==a.itemType }'>selected</c:if> ><c:if test='${"TRAFFIC"==a.itemType }'>流量</c:if><c:if test='${"ENTITY"==a.itemType }'>实物</c:if></option> 
       </c:forEach>
	</select> 
&nbsp;&nbsp;
	<select name="status">
       <option selected value="">请选择领取状态</option>
       <option value="ARRIVE" <c:if test='${status=="ARRIVE" }'>selected</c:if>>领取成功</option> 
       <option value="SUCCESS" <c:if test='${status=="SUCCESS" }'>selected</c:if>>等待领取</option> 
       <option value="WAIT" <c:if test='${status=="WAIT" }'>selected</c:if>>未领取</option> 
       <option value="FAIL" <c:if test='${status=="FAIL" }'>selected</c:if>>失败</option> 
	</select> &nbsp;&nbsp;
	请输入用户名：
    <input type="text" name="username" id="username"class="abc input-default" placeholder="" value="${username }">&nbsp;&nbsp;
	请输入电话号码：
    <input type="text" name="phone" id="phone"class="abc input-default" placeholder="" value="${phone }">&nbsp;&nbsp;  
     <input type="text" name="pageNo" id="pageNo" value="1" style="display:none">
    <button type="submit" class="btn btn-primary">查询</button>&nbsp;&nbsp;<!--  <button type="button" class="btn btn-success" id="addnew">新增机构</button> -->
</form>
<table class="table table-bordered table-hover definewidth m10" >
    <thead>

      <c:if test="${type=='TRAFFIC'}">
    <tr>
        <th>时间日期</th>
        <th>手机号</th>
        <th>领取状态</th>
        <th>用户名</th>
        <th>领取条件</th>
        <th>奖类</th>
    </tr>
      </c:if>
       <c:if test="${type=='ENTITY'}">
    <tr>
        <th>时间日期</th>
        <th>手机号</th>
        <th>领取状态</th>
        <th>用户名</th>
        <th>领取条件</th>
        <th>奖类</th>
        <th>领取地址</th>
        <th>联系人</th>
    </tr>
      </c:if>

    </thead>
     <c:if test="${! empty lists}">
      <c:if test="${type=='TRAFFIC'}">
    <c:forEach items="${lists}" var="listso">
         <tr>
             <td>${listso.create_time } </td>
              <td>${listso.phone }</td>
              <td><c:if test='${listso.status=="ARRIVE" }'>领取成功</c:if>
              <c:if test='${listso.status=="SUCCESS" }'>等待领取</c:if>
              <c:if test='${listso.status=="WAIT" }'>未领取</c:if>
              <c:if test='${listso.status=="FAIL" }'>领取失败</c:if></td>
             <td>${listso.nickname }</td> 
            <td><c:if test='${listso.source=="JOIN" }'>抽奖领取</c:if>
              <c:if test='${listso.source=="SHARE" }'>分享领取</c:if></td>
            <td><c:if test='${listso.award_type=="TRAFFIC" }'>流量</c:if>
            </td>
        </tr> 
       </c:forEach> 
        </c:if>
        <c:if test="${type=='ENTITY'}">
    <c:forEach items="${lists}" var="listso">
         <tr>
             <td>${listso.create_time } </td>
              <td>${listso.phone }</td>
              <td><c:if test='${listso.status=="ARRIVE" }'>领取成功</c:if>
              <c:if test='${listso.status=="SUCCESS" }'>等待领取</c:if>
              <c:if test='${listso.status=="WAIT" }'>未领取</c:if>
              <c:if test='${listso.status=="FAIL" }'>领取失败</c:if></td>
             <td>${listso.nickname }</td> 
            <td><c:if test='${listso.source=="JOIN" }'>抽奖领取</c:if>
              <c:if test='${listso.source=="SHARE" }'>分享领取</c:if></td>
            <td>
              <c:if test='${listso.award_type=="ENTITY" }'>实物</c:if></td>
              <td>${listso.address }</td>
              <td>${listso.contact }</td>
        </tr> 
       </c:forEach> 
        </c:if>
        </c:if>
        </table>
        <c:if test="${pageTotal>1}">
         <div class="pagination pull-right page">
         <ul>
          <li><a href="${ctx}/serach/selectByType/${rid}?pageNo=1&phone=${phone}&status=${status }&username=${username }&type=${type}">首页</a></li>
          <li><a href="${ctx}/serach/selectByType/${rid}?pageNo=${pageProv }&phone=${phone}&status=${status }&username=${username }&type=${type}">上一页</a></li>
          <li><a href="${ctx}/serach/selectByType/${rid}?pageNo=${pageNext }&phone=${phone}&status=${status }&username=${username }&type=${type}">下一页</a></li>
          <li><a href="${ctx}/serach/selectByType/${rid}?pageNo=${pageTotal }&phone=${phone}&status=${status }&username=${username }&type=${type}">尾页</a></li>
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
   /* $(function(){
	   $("#chckact").on("change",function(){
		   var acid=$("#chckact").val();
		   window.location.href="${ctx}/index?pageNo=1&activityId="+acid+"";
	   });
   }) */
</script>
</body>
</html>