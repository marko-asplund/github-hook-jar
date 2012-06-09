package fi.aspluma.hookjar.ruby;

import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;


public class RubyServiceProxy implements ServiceProxy {
  private static final Logger logger = LoggerFactory.getLogger(RubyServiceProxy.class);
  private Ruby ruby;
	private IRubyObject service;
	
	RubyServiceProxy(Ruby ruby, IRubyObject service) {
	  this.ruby = ruby;
		this.service = service;
	}
	
	// FIXME
  public void configure() {
    // set smtp etc. parameters
    logger.debug("vars: "+service.getInstanceVariables().getInstanceVariableNameList());
    RubyHash emailConf = (RubyHash) service.callMethod(service.getRuntime().getCurrentContext(), "email_config");
    emailConf.put("address", "localhost");
    logger.debug("keys: "+emailConf.keySet());
//    System.out.println(service.callMethod(ruby.getCurrentContext(), "smtp_address"));
	}
	
  public void processRequest() {
		JavaEmbedUtils.invokeMethod(service.getRuntime(), service, "receive_push", new Object[]{}, Object.class);
	}

}
