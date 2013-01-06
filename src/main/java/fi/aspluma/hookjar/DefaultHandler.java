package fi.aspluma.hookjar;

import java.util.HashMap;
import java.util.Map;

/**
 * Default {@link Handler} interface implementation.
 * 
 * @author aspluma
 */
public class DefaultHandler implements Handler {
  private HandlerType type;
  private String className;
  private ProxyInitializer initializer;
  private Map<String, String> parameters = new HashMap<String, String>();

  public DefaultHandler(HandlerType handlerType, String className, ProxyInitializer initializer) {
    this.type = handlerType;
    this.className = className;
    this.initializer = initializer;
  }

  public DefaultHandler(HandlerType handlerType, String className) {
  	this(handlerType, className, null);
  }
  
  @Override
  public HandlerType getType() {
    return type;
  }
  
  @Override
  public String getClassName() {
    return className;
  }

  @Override
  public Map<String, String> addParameter(String key, String value) {
    parameters.put(key, value);
    return parameters;
  }
  
  @Override
  public Map<String, String> getParameters() {
    return parameters;
  }

	@Override
  public ProxyInitializer getInitializer() {
	  return initializer;
  }
	
}
