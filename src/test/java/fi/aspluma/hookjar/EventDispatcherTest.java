package fi.aspluma.hookjar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.config.Configuration;

public class EventDispatcherTest {
	@SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(EventDispatcherTest.class);

	@Test
	public void testDispatch() throws Exception {
		ServiceProxy p = mock(ServiceProxy.class);
		Configuration c = new TestConfiguration(p);
		EventDispatcher d = new EventDispatcher(c);
		
    byte[] input = IOUtils.toByteArray(new FileInputStream("src/test/resources/sample1.json"));
    d.dispatch("/foo", input);
    
    verify(p).processRequest();
	}

	private class TestConfiguration implements Configuration {
		private ServiceProxy proxy;
		
		public TestConfiguration(ServiceProxy proxy) {
			this.proxy = proxy;
		}
		
		@Override
    public Map<String, HandlerChain> getConfiguredHandlerChains() {
	    Map<String, HandlerChain> chains = new HashMap<String, HandlerChain>();
	    HandlerChain hc1 = new HandlerChain("/foo");
	    
	    DefaultHandler h = new DefaultHandler(HandlerType.JAVA, "dummy");
	    hc1.addHandler(h);

	    chains.put(hc1.getUrl(), hc1);
      return chains;
    }

		@Override
    public Map<HandlerType, ServiceProxyFactory> getServiceProxyFactories() {
			Map<HandlerType, ServiceProxyFactory> f = new HashMap<HandlerType, ServiceProxyFactory>();
			f.put(HandlerType.JAVA, new ServiceProxyFactory() {
				@Override
				public ServiceProxy createServiceProxy(Handler handler, byte[] rawData, Map<?, ?> parsedData) {
					return proxy;
				}
			});
			return f;
    }
	};

	
}
