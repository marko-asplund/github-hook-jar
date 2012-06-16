package fi.aspluma.hookjar.ruby;

import org.jruby.RubyHash;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;


public class RubyServiceProxy implements ServiceProxy {
  private static final Logger logger = LoggerFactory.getLogger(RubyServiceProxy.class);
  private ScriptingContainer ruby;
	private IRubyObject service;
	
	RubyServiceProxy(ScriptingContainer ruby, IRubyObject service) {
	  this.ruby = ruby;
		this.service = service;
	}
	
	// FIXME
  public void configure() {
    // set smtp etc. parameters
    logger.debug("vars: "+service.getInstanceVariables().getInstanceVariableNameList());
    RubyHash emailConf = ruby.callMethod(service,"email_config", RubyHash.class);
    emailConf.put("address", "localhost");
    logger.debug("keys: "+emailConf.keySet());
    logger.debug(ruby.callMethod(service,"smtp_address", String.class));
	}
	
  public void processRequest() {
    ruby.callMethod(service, "receive_push", Object.class);
	}

}
