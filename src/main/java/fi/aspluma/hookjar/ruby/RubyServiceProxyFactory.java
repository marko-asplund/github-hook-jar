package fi.aspluma.hookjar.ruby;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceProxyFactory;

public class RubyServiceProxyFactory implements ServiceProxyFactory {
	private static Logger logger = LoggerFactory.getLogger(RubyServiceProxyFactory.class);
	private Ruby ruby;
	
	public RubyServiceProxyFactory(String rubyHome) throws IOException {
		String[] paths = new String[] {
				".", "lib", "/services", rubyHome+"/lib/ruby/1.8"
		};
		List<String> loadPaths = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(".bundle/loadpath"));
		for(String p : paths)
			loadPaths.add(0, p);
		ruby = JavaEmbedUtils.initialize(loadPaths);

		String scriptFile = "service-boot.rb";
		URL file = this.getClass().getClassLoader().getResource(scriptFile);
		String script = FileUtils.readFileToString(new File(file.getFile()));
		ruby.executeScript(script, scriptFile);
		logger.debug("Factory initialized");
	}
	
	public ServiceProxy createServiceProxy(String serviceName, Map<String, String> config, Map<String, ?> eventData) {
		// instantiate service
		EventDataConverter c = new EventDataConverter(ruby);
		logger.debug("eventData: "+eventData);
		
		IRubyObject[] args = new IRubyObject[] {
				ruby.newSymbol("push"),
				JavaEmbedUtils.javaToRuby(ruby, config),
				c.deepConvert(eventData.get("payload"))
		};
		
		IRubyObject service = getRubyClass(ruby, "Service::CommitMsgChecker").newInstance(ruby.getCurrentContext(), args, Block.NULL_BLOCK);
		return new RubyServiceProxy(ruby, service);
	}

	private static RubyClass getRubyClass(Ruby rt, String clazz) {
		String[] comps = clazz.split("::");
		RubyClass rc = rt.getClass(comps[0]);
		if(comps.length == 1)
			return rc;
		for(int i = 1; i<comps.length; i++)
			rc = rc.getClass(comps[i]);
		return rc;
	}

}
