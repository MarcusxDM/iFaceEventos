package events.exceptionsFile;

public class EventAlreadyExistsException extends Exception {
	public EventAlreadyExistsException() {
		super();
	} 
	  public EventAlreadyExistsException(String message) { super(message); }
}
