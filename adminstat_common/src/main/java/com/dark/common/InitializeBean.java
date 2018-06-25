package com.dark.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.dark.common.domain.Constants;

@Component
public class InitializeBean implements ServletContextListener {

	private static final Log logger = LogFactory.getLog(InitializeBean.class);

	private final String configLocation = "classpath:config.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		org.apache.ibatis.logging.LogFactory.useSlf4jLogging();

		if (logger.isDebugEnabled()) {
			logger.debug("loading properties...");
		}

		ResourceLoader loader = new DefaultResourceLoader();
		Resource resource = loader.getResource(configLocation);
		InputStream is = null;
		Properties prop = new Properties();

		try {
			is = resource.getInputStream();
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Constants.IS_DEBUG = Boolean.valueOf(prop.getProperty("IS_DEBUG",
				"false"));

		Constants.RESOURCE_ROOT_PATH = prop.getProperty("RESOURCE_ROOT_PATH");
		
		Constants.RESOURCE_ROOT_URL = prop.getProperty("RESOURCE_ROOT_URL");
		
		Constants.OSS_ENDPOINT = prop.getProperty("OSS_ENDPOINT");
		
		Constants.TOKEN_STORE_USEREDIS = Boolean.valueOf(prop.getProperty("TOKEN_STORE_USEREDIS"));
		
		Constants.OSS_USE = Boolean.valueOf(prop.getProperty("OSS_USE"));
		
		Constants.REDIS_MASTER_HOST = prop.getProperty("REDIS_MASTER_HOST");
		
		Constants.REDIS_MASTER_PASSWORD = prop.getProperty("REDIS_MASTER_PASSWORD");
		
		Constants.WEICHAT_APPID=prop.getProperty("WEICHAT_APPID");
		
		Constants.WEICHAT_APPSECRET=prop.getProperty("WEICHAT_APPSECRET");
		
		Constants.CALLBACK_URL=prop.getProperty("CALLBACK_URL");
		
		Constants.LIUMI_HOST=prop.getProperty("LIUMI_HOST");
		
		Constants.LIUMI_APPKEY=prop.getProperty("LIUMI_APPKEY");
		
		Constants.LIUMI_APPSECRET=prop.getProperty("LIUMI_APPSECRET");
		
		Constants.TONGJI_HOST_HTTP=prop.getProperty("TONGJI_HOST_HTTP");
		
		Constants.TONGJI_HOST_HTTPS=prop.getProperty("TONGJI_HOST_HTTPS");

		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (prop != null) {
			prop.clear();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loaded properties...");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
