package com.dark.common.domain;

import org.apache.ibatis.session.RowBounds;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Pager extends RowBounds {
	/**
	 * 
	 */

	private Integer pageSize = 0;

	private Integer totalCount = 0;

	private Integer startIndex = 0;
	
	private Integer pageNo = 1;

	public Pager() {
		super();
		this.pageSize = 1;
	}

	public Pager(Integer startIndex) {
		this();
		this.pageSize = 1;
		this.startIndex = startIndex;
	}

	public Pager(Integer pageSize, Integer startIndex) {
		super();
		this.pageSize = pageSize;
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@JsonIgnore
	public int getOffset() {
		return RowBounds.NO_ROW_OFFSET;
	}

	@JsonIgnore
	public int getLimit() {
		return RowBounds.NO_ROW_LIMIT;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
