package fi.aspluma.hookjar.java;

import java.util.Map;

import fi.aspluma.hookjar.ServiceProxy;

/**
 * Java based {@link ServiceProxy} implementation base class.
 * 
 * @author aspluma
 */
public abstract class JavaServiceProxy extends ServiceProxy {
  public abstract void setConfiguration(Map<String, String> config);
  public abstract void setEventData(Map<?, ?> data);
}
