package fi.aspluma.hookjar.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceRunner;
import fi.aspluma.hookjar.ruby.RubyServiceProxyFactory;

@WebServlet("/")
public class ServiceDispatcherServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(ServiceDispatcherServlet.class);
  private RubyServiceProxyFactory serviceFactory;
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.debug("doPost");

    ServiceProxy srv = serviceFactory.createServiceProxy("Service::CommitMsgChecker", ServiceRunner.getConfig(),
        ServiceRunner.getPayload());
    srv.configure();
    srv.processRequest();

  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    serviceFactory = (RubyServiceProxyFactory)config.getServletContext().getAttribute("fi.aspluma.hookjar.serviceFactory");
    logger.debug("serviceFactory: "+serviceFactory);
  }

}
