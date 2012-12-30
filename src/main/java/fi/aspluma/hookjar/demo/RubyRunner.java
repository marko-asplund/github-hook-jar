package fi.aspluma.hookjar.demo;

import java.util.Arrays;

import org.jruby.CompatVersion;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;

public class RubyRunner {

	public static void main(String[] args) {
		ScriptingContainer sc = new ScriptingContainer(LocalContextScope.CONCURRENT);
    sc.setCompatVersion(CompatVersion.RUBY1_8);
    
    sc.setLoadPaths(Arrays.asList(new String[] {"/Users/aspluma/projects/personal/github-hook-jar"}));
    sc.runScriptlet("require 'service'");
    Object clazz = sc.runScriptlet("Service");
    IRubyObject svc = sc.callMethod(clazz, "new", IRubyObject.class);
    
    sc.put("x", svc);
    sc.runScriptlet("puts x\n"+
    		"x.config['a']=99\n"+
    		"puts x.config['a']");
//    sc.runScriptlet("");
    sc.put("x", svc);
    sc.runScriptlet("puts x.config['a']");
    
//    System.out.println(svc);
    
	}

}
