package com.dark.data.domain;

public class statTraffic {
	private String time;
	private String pheader;
	private String openids;
	private String phone;
	
	private String provider;
	private String itemtype;
	private Long lingqu;
	private String status;
	
	private String activityTitle;
	private String network;
	private Long shares;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPheader() {
		return pheader;
	}
	public void setPheader(String pheader) {
		this.pheader = pheader;
	}
	public String getOpenids() {
		return openids;
	}
	public void setOpenids(String openids) {
		this.openids = openids;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getItemtype() {
		return itemtype;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActivityTitle() {
		return activityTitle;
	}
	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public Long getLingqu() {
		return lingqu;
	}
	public void setLingqu(Long lingqu) {
		this.lingqu = lingqu;
	}
	public Long getShares() {
		return shares;
	}
	public void setShares(Long shares) {
		this.shares = shares;
	}
}
