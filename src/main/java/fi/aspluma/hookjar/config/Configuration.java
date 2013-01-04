package fi.aspluma.hookjar.config;

import java.io.IOException;
import java.util.Map;

import fi.aspluma.hookjar.HandlerChain;
import fi.aspluma.hookjar.HandlerType;
import fi.aspluma.hookjar.ServiceProxyFactory;

public interface Configuration {
  Map<String, HandlerChain> getConfiguredHandlerChains();
	Map<HandlerType, ServiceProxyFactory> getServiceProxyFactories() throws IOException;
}
