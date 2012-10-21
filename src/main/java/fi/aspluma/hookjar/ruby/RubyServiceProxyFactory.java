package fi.aspluma.hookjar.ruby;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.CompatVersion;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceProxyFactory;
import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.Gson;


public class RubyServiceProxyFactory implements ServiceProxyFactory {
	private static Logger logger = LoggerFactory.getLogger(RubyServiceProxyFactory.class);
	private ScriptingContainer ruby;
	
	// TODO: make this class a singleton
	public RubyServiceProxyFactory(String rubyHome, String githubServicesHome) throws IOException {
	  
	  StopWatch w = new StopWatch();
	  w.start();
		
	  List<String> loadPaths = null;
	  if(githubServicesHome == null)
      loadPaths = getUnExplodedLoadPaths(rubyHome);
	  else
      loadPaths = getExplodedLoadPaths(rubyHome, githubServicesHome);
		ruby = new ScriptingContainer(LocalContextScope.CONCURRENT);
		ruby.setCompatVersion(CompatVersion.RUBY1_8);
		
		logger.debug("ruby version: "+ruby.getCompatVersion());
		logger.debug("java version: "+System.getProperty("java.version"));
		
		logger.debug("ScriptingContainer created: "+w.toString());
		ruby.setLoadPaths(loadPaths);

		// include some required gems
		String scriptFile = "service-boot.rb";
		URL file = this.getClass().getClassLoader().getResource(scriptFile);
		String script = FileUtils.readFileToString(new File(file.getFile()));
		
		// include all github services
		script += FileUtils.readFileToString(new File(githubServicesHome+"/requires.rb"));
		
		ruby.runScriptlet(script);
    w.stop();

    logger.debug("Factory fully initialized: "+w.toString());
	}

	private List<String> getUnExplodedLoadPaths(String rubyHome) throws IOException {
    // add github-services and Ruby libs to load path
    String[] paths = new String[] {
        "lib", rubyHome+"/lib/ruby/1.8"
    };
    // add Ruby gems used by github-services to load path
    List<String> loadPaths = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(".bundle/loadpath"));
    loadPaths.addAll(0, Arrays.asList(paths));
    return loadPaths;
	}
	
	private List<String> getExplodedLoadPaths(String rubyHome, String githubServicesHome) throws IOException {
    // add github-services and Ruby libs to load path
    String[] paths = new String[] {
        githubServicesHome+"/lib", githubServicesHome, rubyHome+"/lib/ruby/1.8"
    };
    // add Ruby gems used by github-services to load path
    List<String> loadPaths = new ArrayList<String>();
    List<String> gemPaths = FileUtils.readLines(new File(githubServicesHome+"/.bundle/loadpath"));
    for(String p : gemPaths)
      loadPaths.add(githubServicesHome+"/"+p);
    loadPaths.addAll(0, Arrays.asList(paths));
    return loadPaths;
	}
	
	public ServiceProxy createServiceProxy(String serviceName, Map<String, String> config, byte[] eventData) {
		// instantiate service
	  // FIXME
	  Object data;
	  if(false) {
	    Object jsonClass = ruby.runScriptlet("JSON");
	    data = ruby.callMethod(jsonClass, "parse", new String(eventData), IRubyObject.class);
	  } else {
	    data = new Gson().fromJson(new String(eventData), Map.class);
      logger.debug("parsing input data with Gson lib: "+data);
	  }
		
		Object[] args = new Object[] {
				ruby.runScriptlet(":push"), config, data
		};
		Object srv = ruby.runScriptlet(serviceName);
		IRubyObject service = ruby.callMethod(srv, "new", args, IRubyObject.class);
		
		return new RubyServiceProxy(ruby, service);
	}

}
