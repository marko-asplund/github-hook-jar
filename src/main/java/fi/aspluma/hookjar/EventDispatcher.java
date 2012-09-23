package fi.aspluma.hookjar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ruby.RubyServiceProxyFactory;

public class EventDispatcher {
  private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

  private Map<String, HandlerChain> chains = new HashMap<String, HandlerChain>();
  private Map<HandlerType, ServiceProxyFactory> factories = new HashMap<HandlerType, ServiceProxyFactory>();
  
  public EventDispatcher() throws IOException {
    initialize();
  }

  public void dispatch(String requestURI, byte[] input) {
    HandlerChain chain = chains.get(requestURI);
    for(Handler h : chain.getHandlers()) {
      logger.debug("invoking handler: "+h);
      ServiceProxyFactory sf = factories.get(h.getType());
      ServiceProxy srv = sf.createServiceProxy(h.getClassName(), h.getParameters(), input);
      srv.configure();
      srv.processRequest();
    }
  }
  
  private void initialize() throws IOException {
	  String rubyHome = System.getProperty("ghj.ruby.home");
	  String githubServicesHome = System.getProperty("ghj.github-services.home");
	  
	  if(rubyHome == null || githubServicesHome == null) {
	    throw new RuntimeException("ghj.ruby.home and ghj.github-services.home system properties must be set");
	  }
    
    factories.put(HandlerType.RUBY, new RubyServiceProxyFactory(rubyHome, githubServicesHome));
    // TODO: add other factories here
    
    HandlerChain hc1 = new HandlerChain("/foo");
    Handler h = new Handler(HandlerType.RUBY, "Service::CommitMsgChecker");
    h.addParameter("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
    h.addParameter("recipients", "a@b.fi, c@d.fi");
    h.addParameter("subject", "foobar");
    hc1.addHandler(h);
    
    h = new Handler(HandlerType.RUBY, "Service::Jira");
    h.addParameter("server_url", "http://localhost:5050/foobar");
    h.addParameter("api_version", "123");
    h.addParameter("username", "myuser");
    h.addParameter("password", "mypwd");
    hc1.addHandler(h);
    
//    h = new Handler(HandlerType.RUBY, "Service::Email");
//    h.addParameter("address", "a@b.fi c@d.fi");
//    hc1.addHandler(h);
    
    
    chains.put(hc1.getUrl(), hc1);
    // TODO: add other handlers
    
    // TODO: add other chains
  }

}
