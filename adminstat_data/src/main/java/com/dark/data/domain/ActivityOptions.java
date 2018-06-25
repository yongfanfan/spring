package com.dark.data.domain;

import java.io.Serializable;

public class ActivityOptions implements Serializable {
	private static final long serialVersionUID = 5491889542691557741L;

	private Long id;

    private Long activityId;

    private String name;
    
    private String content;

    private String thumb;

    private Integer quantity;

    private String itemType;

    private String summary;

    public ActivityOptions() {
		super();
	}

	public ActivityOptions(Long id, String content,
			String itemType) {
		super();
		this.id = id;
		this.content = content;
		this.itemType = itemType;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb == null ? null : thumb.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}