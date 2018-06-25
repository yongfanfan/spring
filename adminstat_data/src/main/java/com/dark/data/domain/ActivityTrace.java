package com.dark.data.domain;

import java.io.Serializable;
import java.util.Date;

public class ActivityTrace implements Serializable {
	private static final long serialVersionUID = -8143121229342516705L;

	private Long activityId;

    private Long userId;

    private Date lastJoinTime;

    private Date joinTime;

    private Date lastShareTime;

    private Date shareTime;

    private Integer joins;

    private Integer shares;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getLastJoinTime() {
        return lastJoinTime;
    }

    public void setLastJoinTime(Date lastJoinTime) {
        this.lastJoinTime = lastJoinTime;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getLastShareTime() {
        return lastShareTime;
    }

    public void setLastShareTime(Date lastShareTime) {
        this.lastShareTime = lastShareTime;
    }

    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    public Integer getJoins() {
        return joins;
    }

    public void setJoins(Integer joins) {
        this.joins = joins;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }
    
    public boolean haveShared() {
    	if (this.shares > 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean haveJoined() {
    	if (this.joins > 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
}