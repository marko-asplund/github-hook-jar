package fi.aspluma.hookjar;

import java.util.Map;

/**
 * A Handler represents the configuration information of a {@link ServiceProxy}  
 * module that processes incoming post-receive
 * 
 * @author aspluma
 */
public interface Handler {
	public HandlerType getType();
	public String getClassName();
	public Map<String, String> addParameter(String key, String value);
	public Map<String, String> getParameters();
	public ProxyInitializer getInitializer();
}