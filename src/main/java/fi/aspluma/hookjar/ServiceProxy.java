package fi.aspluma.hookjar;

public abstract class ServiceProxy {
  public void configure(ProxyInitializer initializer) {
  	if(initializer != null)
  		initializer.initialize(this);
	}
  public abstract void processRequest();
}
