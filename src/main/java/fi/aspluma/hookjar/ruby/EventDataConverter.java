package fi.aspluma.hookjar.ruby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jruby.RubyArray;
import org.jruby.RubyHash;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.aspluma.hookjar.TypeConverter;

@Deprecated
public class EventDataConverter {
	private Map<Class<?>, TypeConverter<? extends IRubyObject>> converters = new HashMap<Class<?>, TypeConverter<? extends IRubyObject>>();
	private ScriptingContainer ruby;
	@SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(EventDataConverter.class);

	public EventDataConverter(ScriptingContainer ruby) {
		this.ruby = ruby;
		converters.put(java.util.LinkedHashMap.class, new RubyHashConverter());
		converters.put(ArrayList.class, new RubyArrayConverter());
	}

	private class RubyHashConverter implements TypeConverter<RubyHash> {
		public RubyHash convert(Object o) {
			@SuppressWarnings("unchecked")
			Map<String, ?> m = (Map<String, ?>) o;
	    Object rh = ruby.runScriptlet("Hash");
	    RubyHash h = ruby.callMethod(rh, "new", RubyHash.class);
			for (Iterator<String> i = m.keySet().iterator(); i.hasNext();) {
				String k = i.next();
				Object v = m.get(k);
				if (converters.containsKey(m.get(k).getClass()))
					v = deepConvert(v);
				h.put(k, v);
			}
			return h;
		}
	}

	private class RubyArrayConverter implements TypeConverter<RubyArray> {
		public RubyArray convert(Object o) {
			List<?> l = (List<?>) o;
			Object rra = ruby.runScriptlet("Array");
			RubyArray ra = ruby.callMethod(rra, "new", RubyArray.class);
			for (Object lo : l) {
				Object v = lo;
				if (converters.containsKey(v.getClass()))
					v = deepConvert(v.getClass().cast(v));
				ra.add(v);
			}
			return ra;
		}
	}

	public IRubyObject deepConvert(Object obj) {
		TypeConverter<? extends IRubyObject> c = converters.get(obj.getClass());
		if (c != null)
			return c.convert(obj);
		return null;
	}

}
