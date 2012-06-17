package fi.aspluma.hookjar;

import java.util.HashMap;
import java.util.Map;

public class Handler {
  private HandlerType type;
  private String className;
  private Map<String, String> parameters = new HashMap<String, String>();

  public Handler(HandlerType handlerType, String className) {
    type = handlerType;
    this.className = className;
  }
  
  public HandlerType getType() {
    return type;
  }
  
  public String getClassName() {
    return className;
  }

  public Map<String, String> addParameter(String key, String value) {
    parameters.put(key, value);
    return parameters;
  }
  
  public Map<String, String> getParameters() {
    return parameters;
  }

}
