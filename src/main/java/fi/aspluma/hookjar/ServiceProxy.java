package fi.aspluma.hookjar;

/**
 * Service proxies receive and process post-receive event notifications from GitHub repositories.
 * Proxies are responsible for implementing their own initialization or configuration as well as
 * the processing logic.
 *
 * @author aspluma
 * 
 * @see Handler
 * @see HandlerChain
 * @see ServiceProxyFactory
 */
public abstract class ServiceProxy {
  public void configure(ProxyInitializer initializer) {
  	if(initializer != null)
  		initializer.initialize(this);
	}
  public abstract void processRequest();
}
