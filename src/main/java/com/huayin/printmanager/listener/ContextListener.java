package com.huayin.printmanager.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextListener implements ServletContextListener
{
	private static final Log logger = LogFactory.getLog(ContextListener.class);

	// 站点根路径
	// public static String WEB_PATH = "print.web.path";
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		ServletContext servletContext = arg0.getServletContext();
		logger.info("Shutdowning System:" + servletContext.getServletContextName() + "...................");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		ServletContext servletContext = arg0.getServletContext();
		logger.info("Starting System:" + servletContext.getServletContextName() + "......................");
		// String path = servletContext.getRealPath("/");
		// System.setProperty(SysConstants.WEB_PATH, path);
	}
}
