
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class MyService {
	private static Ruby rt;
	
	public void initialize() {
		rt = Ruby.newInstance();
		rt.getLoadService().load("rubyservice.rb", false);
	}

	public void processRequest(Map<String, String> requestParameters) {
		IRubyObject rubyService = rt.evalScriptlet("RubyService.new");
		IRubyObject r = rubyService.callMethod(rt.getCurrentContext(), "process", JavaUtil.convertJavaToRuby(rt, requestParameters));
		System.out.println("return: "+r.asJavaString());
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
