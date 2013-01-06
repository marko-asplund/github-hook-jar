package fi.aspluma.hookjar;

import java.util.Map;

/**
 * Defines a factory class interface for classes instantiating {@link ServiceProxy ServiceProxies}.
 * 
 * @author aspluma
 * 
 * @see ServiceProxy
 */
public interface ServiceProxyFactory {
	ServiceProxy createServiceProxy(Handler handler, byte[] rawData, Map<?, ?> parsedData);
}
