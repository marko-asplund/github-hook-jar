package fi.aspluma.hookjar.ruby;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ProxyInitializer;
import fi.aspluma.hookjar.ServiceProxy;

public class RubyProxyInitializer implements ProxyInitializer {
	private static final Logger logger = LoggerFactory.getLogger(RubyProxyInitializer.class);
	private String scriptlet;
	
	public RubyProxyInitializer(String scriptlet) {
		this.scriptlet = scriptlet;
	}
	
	@Override
	public void initialize(ServiceProxy proxy) {
		logger.debug("initialize: "+proxy);
		RubyServiceProxy p = (RubyServiceProxy)proxy;
		p.runScriptlet(scriptlet);
	}

}
