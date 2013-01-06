package fi.aspluma.hookjar;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import fi.aspluma.hookjar.config.Configuration;

/**
 * Receives post-receive event notifications, selects a {@link HandlerChain} and dispatches the event to
 * the correct chain and {@link ServiceProxy ServiceProxies} in that chain for processing.
 * 
 * @author aspluma
 */
public class EventDispatcher {
  private static final Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

  private Map<String, HandlerChain> chains = new HashMap<String, HandlerChain>();
  private Map<HandlerType, ServiceProxyFactory> factories;
  
  public EventDispatcher(Configuration config) {
    initialize(config);
  }

  public void dispatch(String requestURI, byte[] eventData) {
    Map<?, ?> data = new Gson().fromJson(new String(eventData), Map.class);
    logger.debug("parsing input data with Gson lib: "+data);
    
    HandlerChain chain = chains.get(requestURI);  // NB: exact match
    if(chain == null)
    	throw new FaultException("no chain found for URI: "+requestURI);
    for(Handler h : chain.getHandlers()) {
      logger.debug("invoking handler: "+h);

      /* the handler chain could be pre-populated before dispatch */
      ServiceProxyFactory sf = factories.get(h.getType());
      ServiceProxy svc = sf.createServiceProxy(h, eventData, data);
      svc.configure(h.getInitializer());
      svc.processRequest();
    }
  }
  
  private void initialize(Configuration config) {
	  factories = config.getServiceProxyFactories();
	  chains = config.getConfiguredHandlerChains();
  }
  
}
