
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
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;

public class MyService {
	private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7/lib/ruby/1.8";
	private static final String GS = "github-services";
	private static final String GEMS = "github-services/vendor/gems/ruby/1.8/gems";
	private static Ruby rt;
	
	public void initialize() throws IOException {
		
//		export LP=$H:$G/liquid-2.3.0/lib:$H/lib:$G/addressable-2.2.7/lib:$G/faraday-0.7.6/lib:$G/rack-1.4.1/lib:$R:$G/sinatra-1.2.6/lib:\
//				$G/tilt-1.2.2/lib:$G/tinder-1.7.0/lib:$G/activesupport-3.0.10/lib:$G/faraday_middleware-0.8.7/lib:$G/xmpp4r-0.5/lib:\
//				$G/i18n-0.5.0/lib:$G/mail-2.3.0/lib:$G/mime-types-1.18/lib:$G/treetop-1.4.10/lib

		String[] paths = new String[] {
				RUBY_HOME, GS+"/lib", GS+"/services", GEMS+"/liquid-2.3.0/lib", GEMS+"/activesupport-3.0.10/lib",
				GEMS+"/i18n-0.5.0/lib", GEMS+"/mail-2.3.0/lib", GEMS+"/mime-types-1.18/lib", GEMS+"/treetop-1.4.10/lib",
				GEMS+"/addressable-2.2.7/lib", GEMS+"/faraday-0.7.6/lib", GEMS+"/rack-1.4.1/lib", GEMS+"/tinder-1.7.0/lib",
				GEMS+"/faraday_middleware-0.8.7/lib", GEMS+"/xmpp4r-0.5/lib"
		};
//		System.out.println("paths: "+Arrays.asList(paths));
		rt = JavaEmbedUtils.initialize(Arrays.asList(paths));
		
		String scriptFile = "service-boot.rb";
		rt.executeScript(FileUtils.readFileToString(new File(scriptFile)), scriptFile);
		System.out.println("cl: "+rt.getClass("CommitMsgChecker"));
	}

	public void processRequest(Map<String, String> requestParameters) {
//		IRubyObject rubyService = rt.evalScriptlet("Service::CommitMsgChecker.new");
		
		Map<String, String> data = getConfig();
//		System.out.println("data: "+JavaEmbedUtils.javaToRuby(rt, data).
		
		Map payload = getPayload();
		IRubyObject[] args = new IRubyObject[] {
				rt.newSymbol("push"), JavaEmbedUtils.javaToRuby(rt, data), JavaEmbedUtils.javaToRuby(rt, payload)
		};
		
		IRubyObject o = rt.getClass("CommitMsgChecker").newInstance(rt.getCurrentContext(), args, Block.NULL_BLOCK);
//		IRubyObject o = rubyService.getMetaClass().newInstance(rt.getCurrentContext(), args, Block.NULL_BLOCK);
		
		String e = (String)JavaEmbedUtils.invokeMethod(rt, o, "event", null, String.class);
		System.out.println("e: "+e);
//		JavaEmbedUtils.invokeMethod(rt, o, "receive_push", new Object[]{}, Object.class);
		
//		IRubyObject[] a = new IRubyObject[] { rt.newString("hello, world") };
//		IRubyObject f = rt.getClass("Foo").newInstance(rt.getCurrentContext(), a, Block.NULL_BLOCK);
//		System.out.println("greet: "+JavaEmbedUtils.invokeMethod(rt, f, "greet", new Object[]{}, String.class));

//		IRubyObject r = rubyService.callMethod(rt.getCurrentContext(), "receive_push");
//		System.out.println("return: "+r.asJavaString());
	}
	
	private Map getConfig() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
		data.put("recipients", "a@b.fi, c@d.fi");
		data.put("subject", "foobar");
		
		return data;
	}

	private Map getPayload() {
		Reader r = null;
		try {
			r = new FileReader(new File("github-services/docs/github_payload"));
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
