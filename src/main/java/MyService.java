
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;

import com.google.gson.Gson;

import fi.aspluma.hookjar.ruby.EventDataConverter;

public class MyService {
	private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7.2";
	private static Ruby rt;
	
	public void initialize() throws IOException {
		String[] paths = new String[] {
				".", "lib", "/services", RUBY_HOME+"/lib/ruby/1.8"
		};
		List<String> loadPaths = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(".bundle/loadpath"));
		for(String p : paths)
			loadPaths.add(0, p);
		rt = JavaEmbedUtils.initialize(loadPaths);
		
		String scriptFile = "service-boot.rb";
		String script = FileUtils.readFileToString(new File(scriptFile));
		rt.executeScript(script, scriptFile);
		System.out.println("initialize done");
	}

	public void processRequest(Map<String, String> requestParameters) {
		// instantiate service
		Map<String, String> data = getConfig();
		@SuppressWarnings("rawtypes")
		Map eventData = getPayload();
		EventDataConverter c = new EventDataConverter(rt);
		System.out.println("eventData: "+eventData);
		
		IRubyObject[] args = new IRubyObject[] {
				rt.newSymbol("push"),
				JavaEmbedUtils.javaToRuby(rt, data),
				c.deepConvert(eventData.get("payload"))
		};
		
		IRubyObject o = getRubyClass(rt, "Service::CommitMsgChecker").newInstance(rt.getCurrentContext(), args, Block.NULL_BLOCK);
		
		// set smtp etc. parameters
		
		System.out.println("vars: "+o.getInstanceVariables().getInstanceVariableNameList());
//		o.callMethod(rt.getCurrentContext(), "@smtp_address", new IRubyObject[]{rt.newString("abc") });
		RubyHash emailConf = (RubyHash) o.callMethod(rt.getCurrentContext(), "email_config");
		emailConf.put("address", "localhost");
		System.out.println("keys: "+emailConf.keySet());
		System.out.println(o.callMethod(rt.getCurrentContext(), "smtp_address"));
		
		String e = (String)JavaEmbedUtils.invokeMethod(rt, o, "event", null, String.class);
		System.out.println("e: "+e);
		JavaEmbedUtils.invokeMethod(rt, o, "receive_push", new Object[]{}, Object.class);
	}

	private Map<String, String> getConfig() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
		data.put("recipients", "a@b.fi, c@d.fi");
		data.put("subject", "foobar");
		
		return data;
	}

	@SuppressWarnings("rawtypes")
	private Map getPayload() {
		Reader r = null;
		try {
			r = new FileReader(new File("github-services/docs/github_payload"));
			Gson g = new Gson();
			Map data = g.fromJson(r, Map.class);
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(r!=null)
				IOUtils.closeQuietly(r);
		}
		return null;
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

	
	@SuppressWarnings("unused")
	private IRubyObject getRubyServiceInstance_OBS() {
		System.out.println("getRubyServiceInstance");

		String script = "config = {"
			      +"'message_format' => '\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$',"
			      +"'recipients' => 'a@b.fi, c@d.fi',"
			      +"'subject' => 'foobar',"
			+"}\n";
		script += "hash = { 'commits' => {}, "+
			"'repository' => { 'url' => 'abc'},"
			+"}\n";
		
		script += "Service::CommitMsgChecker.new(:push, config, hash)";
		IRubyObject o = rt.executeScript(script, "xyz.rb");
		return o;
	}
	
	public static void main(String ... args) throws IOException {
		// one-time service initialization
		MyService s = new MyService();
		s.initialize();

		// the following code executes in a request processing loop
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("operation", "placeOrder");
		s.processRequest(hm);
	}

}
