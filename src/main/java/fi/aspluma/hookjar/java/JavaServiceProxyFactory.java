package fi.aspluma.hookjar.java;

import java.util.Map;

import fi.aspluma.hookjar.Handler;
import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceProxyFactory;

/**
 * Java {@link ServiceProxyFactory} implementation base class.
 * 
 * @author aspluma
 */
public class JavaServiceProxyFactory implements ServiceProxyFactory {

	@Override
	public ServiceProxy createServiceProxy(Handler h, byte[] rawData, Map<?, ?> parsedData) {
    try {
      Class<?> c = Class.forName(h.getClassName());
      JavaServiceProxy p = (JavaServiceProxy) c.newInstance();
      p.setConfiguration(h.getParameters());
      p.setEventData(parsedData);
      return p;
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("failed to initialize java service proxy", e);
    }
  }

}
