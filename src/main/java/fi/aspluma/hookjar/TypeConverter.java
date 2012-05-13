package fi.aspluma.hookjar;

public interface TypeConverter<T> {
	T convert(Object o);
}
