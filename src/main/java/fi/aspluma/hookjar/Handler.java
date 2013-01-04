package fi.aspluma.hookjar;

import java.util.Map;

public interface Handler {
	public HandlerType getType();
	public String getClassName();
	public Map<String, String> addParameter(String key, String value);
	public Map<String, String> getParameters();
	public ProxyInitializer getInitializer();
}