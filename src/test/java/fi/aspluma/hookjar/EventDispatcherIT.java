package fi.aspluma.hookjar;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.config.Configuration;
import fi.aspluma.hookjar.config.StaticJavaConfiguration;

public class EventDispatcherIT {
	@SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(EventDispatcherIT.class);

	@Test
	public void testDispatch() throws Exception {
		
		Configuration c = new StaticJavaConfiguration();
		EventDispatcher d = new EventDispatcher(c);

		byte[] input = IOUtils.toByteArray(new FileInputStream("src/test/resources/sample1.json"));
    d.dispatch("/foo", input);
    
	}

	
}
