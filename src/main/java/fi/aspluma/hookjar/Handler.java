package fi.aspluma.hookjar;

import java.util.HashMap;
import java.util.Map;

public class Handler {
  private HandlerType type;
  private String className;
  private ProxyInitializer initializer;
  private Map<String, String> parameters = new HashMap<String, String>();

  public Handler(HandlerType handlerType, String className, ProxyInitializer initializer) {
    this.type = handlerType;
    this.className = className;
    this.initializer = initializer;
  }

  public Handler(HandlerType handlerType, String className) {
  	this(handlerType, className, null);
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

	public ProxyInitializer getInitializer() {
	  return initializer;
  }

}
