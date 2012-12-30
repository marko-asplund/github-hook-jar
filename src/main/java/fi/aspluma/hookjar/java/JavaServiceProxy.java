package fi.aspluma.hookjar.java;

import java.util.Map;

import fi.aspluma.hookjar.ServiceProxy;

public abstract class JavaServiceProxy extends ServiceProxy {
  public abstract void setConfiguration(Map<String, String> config);
  public abstract void setEventData(Map<?, ?> data);
}
