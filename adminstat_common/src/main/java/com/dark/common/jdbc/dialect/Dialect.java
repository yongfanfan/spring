package com.dark.common.jdbc.dialect;

public interface Dialect {

	/**
	 * 获取分页Sql
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getLimitString(String sql, int offset, int limit);

	/**
	 * 获取总是Sql
	 * 
	 * @param sql
	 * @return
	 */
	public abstract String getCountString(String sql);

}
