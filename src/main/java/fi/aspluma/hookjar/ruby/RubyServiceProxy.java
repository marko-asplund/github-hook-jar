package fi.aspluma.hookjar.ruby;

import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;


public class RubyServiceProxy implements ServiceProxy {
  private static final Logger logger = LoggerFactory.getLogger(RubyServiceProxy.class);
	private IRubyObject service;
	
	RubyServiceProxy(IRubyObject service) {
		this.service = service;
	}
	
	// FIXME
  public void configure() {
    // set smtp etc. parameters
    logger.debug("vars: "+service.getInstanceVariables().getInstanceVariableNameList());
    RubyHash emailConf = (RubyHash) service.callMethod(service.getRuntime().getCurrentContext(), "email_config");
    emailConf.put("address", "localhost");
    logger.debug("keys: "+emailConf.keySet());
    logger.debug(service.callMethod(service.getRuntime().getCurrentContext(), "smtp_address").toString());
	}
	
  public void processRequest() {
		JavaEmbedUtils.invokeMethod(service.getRuntime(), service, "receive_push", new Object[]{}, Object.class);
	}

}
