
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;

import com.google.gson.Gson;

import fi.aspluma.hookjar.ruby.EventDataConverter;

public class MyService {
	private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7/lib/ruby/1.8";
	private static final String GS = "github-services";
	private static final String GEMS = "github-services/vendor/gems/ruby/1.8/gems";
	private static Ruby rt;
	
	public void initialize() throws IOException {
		String[] paths = new String[] {
				RUBY_HOME, ".", GS+"/lib", GS+"/services", GEMS+"/liquid-2.3.0/lib", GEMS+"/activesupport-3.0.10/lib",
				GEMS+"/i18n-0.5.0/lib", GEMS+"/mail-2.3.0/lib", GEMS+"/mime-types-1.18/lib", GEMS+"/treetop-1.4.10/lib",
				GEMS+"/addressable-2.2.7/lib", GEMS+"/faraday-0.7.6/lib", GEMS+"/rack-1.4.1/lib", GEMS+"/tinder-1.7.0/lib",
				GEMS+"/faraday_middleware-0.8.7/lib", GEMS+"/xmpp4r-0.5/lib"
		};
		rt = JavaEmbedUtils.initialize(Arrays.asList(paths));
		
		String scriptFile = "service-boot.rb";
		String script = FileUtils.readFileToString(new File(scriptFile));
		rt.executeScript(script, scriptFile);
	}

	public void processRequest(Map<String, String> requestParameters) {
		// instantiate service
		Map<String, String> data = getConfig();
		@SuppressWarnings("rawtypes")
		Map eventData = getPayload();
		EventDataConverter c = new EventDataConverter(rt);
		
		IRubyObject[] args = new IRubyObject[] {
				rt.newSymbol("push"),
				JavaEmbedUtils.javaToRuby(rt, data),
				c.deepConvert(eventData.get("payload"))
		};
		
		IRubyObject o = getRubyClass(rt, "Service::CommitMsgChecker").newInstance(rt.getCurrentContext(), args, Block.NULL_BLOCK);
		
		// set smtp etc. parameters
//		o.callMethod(rt.getCurrentContext(), "smtp_address=", rt.newString("abc"));
		System.out.println("vars: "+o.getInstanceVariables().getInstanceVariableNameList());
//		JavaEmbedUtils.newObjectAdapter().setInstanceVariable(o, "smtp_address", rt.newString("abc"));
		
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
			r = new FileReader(new File("github-services.dir/docs/github_payload"));
			Gson g = new Gson();
			Map data = g.fromJson(r, Map.class);
			return data;
		} catch (FileNotFoundException e) {
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
