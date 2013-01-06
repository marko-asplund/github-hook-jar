package fi.aspluma.hookjar.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.EventDispatcher;

/**
 * A {@link HttpServlet} subclass that receives incoming post-receive event notifications from GitHub repositories
 * via HTTP and dispatches the HTTP requests to {@link EventDispatcher} for processing.
 * 
 * @author aspluma
 */
@WebServlet("/")
public class EventReceiverServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(EventReceiverServlet.class);
  private EventDispatcher eventDispatcher;
  
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    eventDispatcher = (EventDispatcher)config.getServletContext().getAttribute("fi.aspluma.hookjar.eventDispatcher");
    logger.debug("serviceFactory: "+eventDispatcher);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String path = req.getRequestURI().replaceFirst(this.getServletContext().getContextPath(), "");
    logger.debug("doPost: "+path);

    byte[] input = IOUtils.toByteArray(req.getInputStream());
    eventDispatcher.dispatch(path, input);
    
  }
  
}
