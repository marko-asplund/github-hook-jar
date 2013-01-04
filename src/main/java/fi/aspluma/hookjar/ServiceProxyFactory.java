package fi.aspluma.hookjar;

import java.util.Map;

public interface ServiceProxyFactory {
	ServiceProxy createServiceProxy(Handler handler, byte[] rawData, Map<?, ?> parsedData);
}
