package com.dark.data.domain;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {
	private static final long serialVersionUID = 9068654890577969908L;

	private Long id;

    private String externalKey;

    private String title;

    private String imgUrl;

    private String backUrl;

    private String newsUrl;

    private String qrcode;

    private Date beginTime;

    private Date endTime;

    private String interval;

    private Integer effectives;

    private String expand;

    private String template;

    private Date createTime;

    private String introduction;

    private String ruleinfo;
    //
    private int joins;
    private int receives;
    private int shares;
    private int nonejoins;
    //
    private int ticket_total;
    private int ticket_rest;
    private int total_send;
    private int total_qiang;
    private int total_share;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey == null ? null : externalKey.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl == null ? null : backUrl.trim();
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl == null ? null : newsUrl.trim();
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode == null ? null : qrcode.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval == null ? null : interval.trim();
    }

    public Integer getEffectives() {
        return effectives;
    }

    public void setEffectives(Integer effectives) {
        this.effectives = effectives;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand == null ? null : expand.trim();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getRuleinfo() {
        return ruleinfo;
    }

    public void setRuleinfo(String ruleinfo) {
        this.ruleinfo = ruleinfo == null ? null : ruleinfo.trim();
    }

	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public int getReceives() {
		return receives;
	}

	public void setReceives(int receives) {
		this.receives = receives;
	}

	public int getJoins() {
		return joins;
	}

	public void setJoins(int joins) {
		this.joins = joins;
	}

	public int getNonejoins() {
		return nonejoins;
	}

	public void setNonejoins(int nonejoins) {
		this.nonejoins = nonejoins;
	}

	public int getTicket_rest() {
		return ticket_rest;
	}

	public void setTicket_rest(int ticket_rest) {
		this.ticket_rest = ticket_rest;
	}

	public int getTicket_total() {
		return ticket_total;
	}

	public void setTicket_total(int ticket_total) {
		this.ticket_total = ticket_total;
	}

	public int getTotal_send() {
		return total_send;
	}

	public void setTotal_send(int total_send) {
		this.total_send = total_send;
	}

	public int getTotal_qiang() {
		return total_qiang;
	}

	public void setTotal_qiang(int total_qiang) {
		this.total_qiang = total_qiang;
	}

	public int getTotal_share() {
		return total_share;
	}

	public void setTotal_share(int total_share) {
		this.total_share = total_share;
	}
}