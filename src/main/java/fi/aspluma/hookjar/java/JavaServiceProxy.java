package fi.aspluma.hookjar.java;

import java.util.Map;

import fi.aspluma.hookjar.ServiceProxy;

public interface JavaServiceProxy extends ServiceProxy {
  void setConfiguration(Map<String, String> config);
  void setEventData(Map<?, ?> data);
}
