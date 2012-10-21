package fi.aspluma.hookjar.config;

import java.util.HashMap;
import java.util.Map;

import fi.aspluma.hookjar.Handler;
import fi.aspluma.hookjar.HandlerChain;
import fi.aspluma.hookjar.HandlerType;

public class StaticJavaConfiguration implements Configuration {

  @Override
  public Map<String, HandlerChain> getConfiguredHandlerChains() {
    Map<String, HandlerChain> chains = new HashMap<String, HandlerChain>();
    
    HandlerChain hc1 = new HandlerChain("/foo");
    Handler h = new Handler(HandlerType.RUBY, "Service::CommitMsgChecker");
    h.addParameter("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
    h.addParameter("recipients", "a@b.fi, c@d.fi");
    h.addParameter("subject", "foobar");
    hc1.addHandler(h);
    
    h = new Handler(HandlerType.RUBY, "Service::Jira");
    h.addParameter("server_url", "http://127.0.0.1:5050/foobar");
    h.addParameter("api_version", "123");
    h.addParameter("username", "myuser");
    h.addParameter("password", "mypwd");
    hc1.addHandler(h);
    
    h = new Handler(HandlerType.JAVA, "fi.aspluma.hookjar.java.services.DemoLogService");
    h.addParameter("param1", "val1");
    hc1.addHandler(h);

    chains.put(hc1.getUrl(), hc1);
    
//    h = new Handler(HandlerType.RUBY, "Service::Email");
//    h.addParameter("address", "a@b.fi c@d.fi");
//    hc1.addHandler(h);
    
    // TODO: add other handlers
    
    // TODO: add other chains
    
    return chains;
  }

}
