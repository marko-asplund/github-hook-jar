package fi.aspluma.hookjar;

import java.util.ArrayList;
import java.util.List;

/**
 * A handler chain represents an ordered sequence of configured {@link Handler}s
 * that has been bound to a particular URL prefix.
 * 
 * @author aspluma
 */
public class HandlerChain {
  private String url;
  private List<Handler> handlers = new ArrayList<Handler>();

  public HandlerChain(String url) {
    this.url = url;
  }
  
  public String getUrl() {
    return url;
  }
  
  public List<Handler> getHandlers() {
    return handlers;
  }

  public void addHandler(Handler handler) {
    handlers.add(handler);
  }
  
}
