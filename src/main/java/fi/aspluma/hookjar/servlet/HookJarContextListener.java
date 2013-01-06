package fi.aspluma.hookjar.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.EventDispatcher;
import fi.aspluma.hookjar.config.StaticJavaConfiguration;

/**
 * Initializes github-hook-jar on servlet context startup.
 * 
 * @author aspluma
 */
@WebListener
public class HookJarContextListener implements ServletContextListener {
  private static final Logger logger = LoggerFactory.getLogger(HookJarContextListener.class);
  private EventDispatcher eventDispatcher;

  public void contextInitialized(ServletContextEvent sce) {
    logger.debug("contextInitialized");
    try {
      eventDispatcher = new EventDispatcher(new StaticJavaConfiguration());
      sce.getServletContext().setAttribute("fi.aspluma.hookjar.eventDispatcher", eventDispatcher);
    } catch (Exception e) {
      logger.error("failed to instantiate EventDispatcher", e);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    logger.debug("contextDestroyed: "+eventDispatcher);
  }
  
}
