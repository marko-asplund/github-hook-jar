package fi.aspluma.hookjar.config;

import java.util.Map;

import fi.aspluma.hookjar.HandlerChain;

public interface Configuration {
  Map<String, HandlerChain> getConfiguredHandlerChains();
}
