package fi.aspluma.hookjar.java.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.java.JavaServiceProxy;

/**
 * A Java-based {@link ServiceProxy} implementation class meant for validation and demonstrative purposes.
 * 
 * @author aspluma
 */
public class DemoLogService extends JavaServiceProxy {
  private static final Logger logger = LoggerFactory.getLogger(DemoLogService.class);
  @SuppressWarnings("unused")
  private Map<String, String> config;
  private Map<?, ?> eventData;

  @Override
  public void processRequest() {
    logger.debug("processRequest");
    
    for(Map.Entry<?, ?> e : eventData.entrySet()) {
      logger.debug("data: "+e.getKey()+"="+e.getValue());
    }
  }

  @Override
  public void setConfiguration(Map<String, String> config) {
    this.config = config;
  }

  @Override
  public void setEventData(Map<?, ?> eventData) {
    this.eventData = eventData;
  }

}
