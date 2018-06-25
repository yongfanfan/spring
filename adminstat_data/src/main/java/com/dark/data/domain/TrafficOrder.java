package com.dark.data.domain;

import java.io.Serializable;
import java.util.Date;

public class TrafficOrder implements Serializable {
    private Long id;

    private Long userId;
    
    private Long activityId;

    private String postpackage;

    private String phone;

    private String status;

    private String flowNo;

    private Date createTime;

    private Date receiveTime;
    
    private String remark;
    
    private String network;
    
    private String province;
    
    private String time;
	private String pheader;
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

	public Long getLingqu() {
		return lingqu;
	}

	public void setLingqu(Long lingqu) {
		this.lingqu = lingqu;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String openids;
	private String provider;
	private String itemtype;
	private Long lingqu;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPostpackage() {
        return postpackage;
    }

    public void setPostpackage(String postpackage) {
        this.postpackage = postpackage == null ? null : postpackage.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo == null ? null : flowNo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
}