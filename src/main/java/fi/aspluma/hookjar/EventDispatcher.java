package fi.aspluma.hookjar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ruby.RubyServiceProxyFactory;

public class EventDispatcher {
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);
  private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7.2";
  private static final String GITHUB_SERVICES_HOME =
      "/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/tmp/github-services";
//      "/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/github-services-1";

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
    factories.put(HandlerType.RUBY, new RubyServiceProxyFactory(RUBY_HOME, GITHUB_SERVICES_HOME));
    // TODO: add other factories here
    
    HandlerChain hc1 = new HandlerChain("/foo");
    Handler h = new Handler(HandlerType.RUBY, "Service::CommitMsgChecker");
    h.addParameter("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
    h.addParameter("recipients", "a@b.fi, c@d.fi");
    h.addParameter("subject", "foobar");
    hc1.addHandler(h);
    
    h = new Handler(HandlerType.RUBY, "Service::Jira");
    h.addParameter("server_url", "http://localhost:5050/foobar");
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
