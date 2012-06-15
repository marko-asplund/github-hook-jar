import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import fi.aspluma.hookjar.ServiceProxy;
import fi.aspluma.hookjar.ServiceProxyFactory;
import fi.aspluma.hookjar.ruby.RubyServiceProxyFactory;


public class ServiceRunner {
  private static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7.2";
  private static final String GITHUB_SERVICES_HOME = "/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/github-services-1";

  public static void main(String[] args) throws IOException {

    ServiceProxyFactory f = new RubyServiceProxyFactory(RUBY_HOME, GITHUB_SERVICES_HOME);
    ServiceProxy srv = f.createServiceProxy("Service::CommitMsgChecker", getConfig(), getPayload());
    srv.configure();
    srv.processRequest();
  }

  private static Map<String, String> getConfig() {
    Map<String, String> data = new HashMap<String, String>();
    data.put("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
    data.put("recipients", "a@b.fi, c@d.fi");
    data.put("subject", "foobar");
    
    return data;
  }

  private static Map<String, ?> getPayload() {
    Reader r = null;
    try {
      r = new FileReader(new File("github-services-1/docs/github_payload"));
      Gson g = new Gson();
      @SuppressWarnings("unchecked")
      Map<String, ?> data = g.fromJson(r, Map.class);
      return data;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      if(r!=null)
        IOUtils.closeQuietly(r);
    }
    return null;
  }



}
