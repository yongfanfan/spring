package com.dark.common.jdbc.plugin;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import com.dark.common.domain.Pager;
import com.dark.common.jdbc.dialect.Dialect;
import com.dark.common.jdbc.helper.SqlHelper;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
public class MybatisPaginationInterceptor implements Interceptor {

	private Logger logger = Logger.getLogger(MybatisPaginationInterceptor.class);
	
	private static final int MAPPED_STATEMENT_INDEX = 0;
	private static final int PARAMETER_INDEX = 1;
	private static final int ROWBOUNDS_INDEX = 2;

	 private Dialect dialect;

	public Object intercept(Invocation invocation) throws Throwable {
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement mappedStatement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		final Object parameter = queryArgs[PARAMETER_INDEX];
		RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        /*
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();*/
		if (!(rowBounds instanceof Pager)) {
			if (rowBounds == null)
				invocation.getArgs()[ROWBOUNDS_INDEX] = new RowBounds();
			return invocation.proceed();
		}
		Pager pager = (Pager) rowBounds;
		Integer offset = pager.getStartIndex();
		/*if (pager.getPageNo() > 0) {
			offset = (pager.getPageNo() -1) * pager.getPageSize();
		}*/
		Integer limit = pager.getPageSize();

		final BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		if (boundSql == null || StringUtils.isBlank(boundSql.getSql())) {
			return null;
		}
		String originalSql = boundSql.getSql().trim();
		
		final Executor executor = (Executor) invocation.getTarget();
		Connection connection = executor.getTransaction().getConnection();
		int count = SqlHelper.getCount(mappedStatement, connection, parameter, dialect);
		pager.setTotalCount(count);

		String limitSql = dialect.getLimitString(originalSql, offset, limit);
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("limit sql: {}", limitSql));
		}
		MappedStatement newMs = newMappedStatement(mappedStatement, boundSql, limitSql);

		queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
		queryArgs[MAPPED_STATEMENT_INDEX] = newMs;

		return invocation.proceed();

	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        if(StringUtils.isBlank(dialectClass)){
        	dialectClass = properties.getProperty("dialect");
        }
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Plug-in [MybatisPaginationInterceptor] cannot create dialect instance by dialectClass: " + dialectClass);
        }

    }
	
	private MappedStatement newMappedStatement(final MappedStatement ms,
			final BoundSql boundSql, final String sql) {
		BoundSql newBoundSql = newBoundSql(ms, boundSql, sql);
		RawSqlSource sqlSource = new RawSqlSource(newBoundSql);
		MappedStatement.Builder builder = new MappedStatement.Builder(
				ms.getConfiguration(), ms.getId(), sqlSource,
				ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		String[] keyProperties = ms.getKeyProperties();
		builder.keyProperty(keyProperties == null ? null : keyProperties[0]);

		// setStatementTimeout()
		builder.timeout(ms.getTimeout());

		// setStatementResultMap()
		builder.parameterMap(ms.getParameterMap());

		// setStatementResultMap()
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());

		// setStatementCache()
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	private BoundSql newBoundSql(final MappedStatement ms,
			final BoundSql boundSql, final String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());

		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop,
						boundSql.getAdditionalParameter(prop));
			}
		}

		return newBoundSql;
	}

	public class RawSqlSource implements SqlSource {
		private BoundSql boundSql;

		public RawSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

}