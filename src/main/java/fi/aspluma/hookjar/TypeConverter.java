package fi.aspluma.hookjar;

@Deprecated
public interface TypeConverter<T> {
	T convert(Object o);
}
