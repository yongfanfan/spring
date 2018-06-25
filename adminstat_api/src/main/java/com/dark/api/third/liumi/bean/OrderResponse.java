package com.dark.api.third.liumi.bean;

import org.apache.commons.lang.StringUtils;

public class OrderResponse {
	private String code;
	private String flowno;
	private Boolean isSuccess=false;
	private Boolean isFinished=false;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFlowno() {
		return flowno;
	}
	public void setFlowno(String flowno) {
		this.flowno = flowno;
	}
	public Boolean isSuccess(){
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Boolean isFinished(){
		return this.isFinished;
	}
	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}
	public String getDesc(){
		if(StringUtils.isEmpty(code)){
			return "";
		}
		else if("000".equals(code)){
			this.isSuccess=true;
			return "成功";
		}
		else if("001".equals(code)){
			return "鉴权参数缺失";
		}
		else if("002".equals(code)){
			return "流量包ID为空";
		}
		else if("003".equals(code)){
			return "手机号为空";
		}
		else if("004".equals(code)){
			return "流量包ID错误";
		}
		else if("005".equals(code)){
			return "手机号码格式错误";
		}
		else if("007".equals(code)){
			return "鉴权信息错误";
		}
		else if("008".equals(code)){
			return "token验证失败";
		}
		else if("009".equals(code)){
			return "签名失败";
		}
		else if("010".equals(code)){
			return "本月超出流量包的叠加次数";
		}
		else if("011".equals(code)){
			return "每秒并发连接过多";
		}
		else if("012".equals(code)){
			return "不符合IP白名单";
		}
		else if("013".equals(code)){
			return "app端口已经失效";
		}
		else if("015".equals(code)){
			return "订单号为空";
		}
		else if("016".equals(code)){
			return "订单号错误";
		}
		else if("018".equals(code)){
			return "日期参数错误";
		}
		else if("021".equals(code)){
			return "业务超出覆盖范围";
		}
		else if("022".equals(code)){
			return "无效的合同或服务已经终止";
		}
		else if("023".equals(code)){
			return "流量包不在合同服务范围内";
		}
		else if("024".equals(code)){
			this.isFinished=true;
			return "流量总额超出合同规定大小";
		}
		else if("025".equals(code)){
			return "单用户合同流量总额超出指定大小";
		}
		else if("026".equals(code)){
			return "单用户日流量总额超过指定大小";
		}
		else if("027".equals(code)){
			return "账号异常";
		}
		else if("028".equals(code)){
			return "单用户下单超过指定合同期内的指定次数";
		}
		else if("029".equals(code)){
			return "IP地址不在白名单内";
		}
		else if("999".equals(code)){
			return "内部错误";
		}
		return "其他错误";
	}
}
