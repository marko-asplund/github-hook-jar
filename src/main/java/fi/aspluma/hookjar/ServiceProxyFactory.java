package fi.aspluma.hookjar;

import java.util.Map;

public interface ServiceProxyFactory {
	ServiceProxy createServiceProxy(String serviceName, Map<String, String> config, Map<String, ?> eventData);
}
