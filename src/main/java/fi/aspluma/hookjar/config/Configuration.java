package fi.aspluma.hookjar.config;

import java.util.Map;

import fi.aspluma.hookjar.HandlerChain;
import fi.aspluma.hookjar.HandlerType;
import fi.aspluma.hookjar.ServiceProxyFactory;

/**
 * A Configuration defines the set of configured hook chains and available proxy factories. 
 * 
 * @author aspluma
 */
public interface Configuration {
  Map<String, HandlerChain> getConfiguredHandlerChains();
	Map<HandlerType, ServiceProxyFactory> getServiceProxyFactories();
}
