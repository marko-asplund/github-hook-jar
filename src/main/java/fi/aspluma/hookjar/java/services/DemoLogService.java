package fi.aspluma.hookjar.java.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.java.JavaServiceProxy;

public class DemoLogService implements JavaServiceProxy {
  private static final Logger logger = LoggerFactory.getLogger(DemoLogService.class);
  private Map<String, String> config;
  private Map<?, ?> eventData;

  @Override
  public void configure() {
    logger.debug("configure: "+config);
  }

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
