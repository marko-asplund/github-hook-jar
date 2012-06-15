package fi.aspluma.hookjar.ruby;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceProxyFactory;

public class RubyServiceProxyFactory implements ServiceProxyFactory {
	private static Logger logger = LoggerFactory.getLogger(RubyServiceProxyFactory.class);
	private ScriptingContainer ruby;
	
	public RubyServiceProxyFactory(String rubyHome, String githubServicesHome) throws IOException {
	  // add github-services and Ruby libs to load path
		String[] paths = new String[] {
		    githubServicesHome+"/lib", githubServicesHome, rubyHome+"/lib/ruby/1.8"
		};
		// add Ruby gems used by github-services to load path
//    List<String> loadPaths = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(".bundle/loadpath"));
		List<String> loadPaths = new ArrayList<String>();
		List<String> gemPaths = FileUtils.readLines(new File(githubServicesHome+"/.bundle/loadpath"));
		for(String p : gemPaths)
			loadPaths.add(githubServicesHome+"/"+p);
		loadPaths.addAll(0, Arrays.asList(paths));
		logger.debug("lp: "+loadPaths);
		
		ruby = new ScriptingContainer(LocalContextScope.CONCURRENT);
		ruby.setLoadPaths(loadPaths);

		String scriptFile = "service-boot.rb";
		URL file = this.getClass().getClassLoader().getResource(scriptFile);
		String script = FileUtils.readFileToString(new File(file.getFile()));
		ruby.runScriptlet(script);
		logger.debug("Factory initialized");
	}
	
	public ServiceProxy createServiceProxy(String serviceName, Map<String, String> config, Map<String, ?> eventData) {
		// instantiate service
		EventDataConverter c = new EventDataConverter(ruby);
		logger.debug("eventData: "+eventData);
		
		Object[] args = new Object[] {
				ruby.runScriptlet(":push"),
				config,
				c.deepConvert(eventData.get("payload"))
		};
		
		Object srv = ruby.runScriptlet(serviceName);
		IRubyObject service = ruby.callMethod(srv, "new", args, IRubyObject.class);
		
		return new RubyServiceProxy(service);
	}

}
