package fi.aspluma.hookjar;

/**
 * Base class for unchecked exceptions.
 * 
 * @author aspluma
 */
public class FaultException extends RuntimeException {
	private static final long serialVersionUID = 2095329035742263383L;

	public FaultException(String message) {
	  super(message);
  }

	public FaultException(String message, Throwable cause) {
		super(message, cause);
  }

}
