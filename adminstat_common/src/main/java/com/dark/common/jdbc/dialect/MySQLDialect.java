package com.dark.common.jdbc.dialect;

import org.apache.log4j.Logger;

public class MySQLDialect implements Dialect {

	private final static Logger logger = Logger.getLogger(MySQLDialect.class);

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append(sql);

		if (offset > 0) {
			pagingSelect.append(" limit ").append(offset).append(',')
					.append(limit);
		} else {
			pagingSelect.append(" limit ").append(limit);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Generated Pager Sql is "
					+ pagingSelect.toString().replaceAll("\r|\n", ""));
		}
		return pagingSelect.toString();
	}

	@Override
	public String getCountString(String sql) {
		StringBuffer countSql = new StringBuffer(sql.length() + 100);
		countSql.append("select count(1) from (").append(sql).append(") t");
		if (logger.isDebugEnabled()) {
			logger.debug("Generated Count Sql is "
					+ countSql.toString().replaceAll("\r|\n", ""));
		}
		return countSql.toString();
	}

}