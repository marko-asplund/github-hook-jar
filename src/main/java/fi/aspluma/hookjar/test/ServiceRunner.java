package fi.aspluma.hookjar.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import fi.aspluma.hookjar.EventDispatcher;


public class ServiceRunner {
  public static final String RUBY_HOME = "/Users/aspluma/projects/personal/git-commit-policy/jruby-1.6.7.2";
  public static final String GITHUB_SERVICES_HOME = "/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/github-services-1";

  public static void main(String[] args) throws IOException {
    EventDispatcher d = new EventDispatcher();
    
    byte[] input = IOUtils.toByteArray(new FileInputStream("/Users/aspluma/projects/personal/git-commit-policy/github-hook-jar/github-services-1/docs/payload_data"));
    d.dispatch("/foo", input);
  }

  @Deprecated
  public static Map<String, String> getConfig() {
    Map<String, String> data = new HashMap<String, String>();
    data.put("message_format", "'\\[#WEB-\\d{1,5} status:\\d+ resolution:\\d+\\] .*$'");
    data.put("recipients", "a@b.fi, c@d.fi");
    data.put("subject", "foobar");
    
    return data;
  }

  @Deprecated
  public static Map<String, ?> getPayload() {
    Reader r = null;
    try {
      r = new FileReader(new File(GITHUB_SERVICES_HOME+"/docs/github_payload"));
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
