package fi.aspluma.hookjar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import fi.aspluma.hookjar.config.Configuration;

public class EventDispatcher {
  private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

  private Map<String, HandlerChain> chains = new HashMap<String, HandlerChain>();
  private Map<HandlerType, ServiceProxyFactory> factories;
  
  public EventDispatcher(Configuration config) throws IOException {
    initialize(config);
  }

  public void dispatch(String requestURI, byte[] eventData) {
    Map<?, ?> data = new Gson().fromJson(new String(eventData), Map.class);
    logger.debug("parsing input data with Gson lib: "+data);
    
    HandlerChain chain = chains.get(requestURI);  // NB: exact match
    if(chain == null)
    	throw new RuntimeException("no chain found for URI: "+requestURI);
    for(Handler h : chain.getHandlers()) {
      logger.debug("invoking handler: "+h);

      ServiceProxyFactory sf = factories.get(h.getType());
      ServiceProxy svc = sf.createServiceProxy(h, eventData, data);
      svc.configure(h.getInitializer());
      svc.processRequest();
    }
  }
  
  private void initialize(Configuration config) throws IOException {
	  factories = config.getServiceProxyFactories();
	  chains = config.getConfiguredHandlerChains();
  }
  
}
