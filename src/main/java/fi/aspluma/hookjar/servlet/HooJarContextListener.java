package fi.aspluma.hookjar.servlet;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ruby.RubyServiceProxyFactory;


@WebListener
public class HooJarContextListener implements ServletContextListener {
  private static final Logger logger = LoggerFactory.getLogger(HooJarContextListener.class);
  private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7.2";
  private static final String GITHUB_SERVICES_HOME = "/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/github-services-1";
  private RubyServiceProxyFactory serviceFactory;

  public void contextInitialized(ServletContextEvent sce) {
    logger.debug("contextInitialized");
    try {
      serviceFactory = new RubyServiceProxyFactory(RUBY_HOME, GITHUB_SERVICES_HOME);
      sce.getServletContext().setAttribute("fi.aspluma.hookjar.serviceFactory", serviceFactory);
    } catch (IOException e) {
      logger.error("failed to instantiate RubyServiceProxyFactory", e);
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    logger.debug("contextDestroyed: "+serviceFactory);
  }

}
