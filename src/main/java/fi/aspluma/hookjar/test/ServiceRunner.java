package fi.aspluma.hookjar.test;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import fi.aspluma.hookjar.EventDispatcher;


public class ServiceRunner {
  public static void main(String[] args) throws IOException {
    EventDispatcher d = new EventDispatcher();
    
    byte[] input = IOUtils.toByteArray(new FileInputStream("src/test/resources/sample1.json"));
    d.dispatch("/foo", input);
  }

}
