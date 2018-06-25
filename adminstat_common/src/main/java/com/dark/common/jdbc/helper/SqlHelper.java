package com.dark.common.jdbc.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dark.common.jdbc.dialect.Dialect;

public abstract class SqlHelper {
    private static Logger logger = LoggerFactory.getLogger(SqlHelper.class);

    public static int getCount(final MappedStatement ms, final Connection connection,
                               final Object parameterObject, Dialect dialect) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String countSql = dialect.getCountString(boundSql.getSql());
        
        if(logger.isDebugEnabled()){
        	 logger.debug("Total count SQL [{}]", countSql);
             logger.debug("Parameters: {} ", parameterObject);
        }

        PreparedStatement stmt = null;
        ResultSet rs;
        try{
            stmt = connection.prepareStatement(countSql);
            DefaultParameterHandler handler = new DefaultParameterHandler(ms, parameterObject, boundSql);
            handler.setParameters(stmt);
            rs = stmt.executeQuery();

            int count = 0;
            if(rs.next()){
                count = rs.getInt(1);
            }

            return count;
        }finally {
            closeStatement(stmt);
        }
    }

    private static void closeStatement(Statement statement){
        if(statement != null){
            try{
                statement.close();
            } catch (SQLException e) {
            	logger.error("close sql statement failed");
            }
        }
    }
}
