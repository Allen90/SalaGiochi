package server.exception;

public class UserException extends Exception {
	
	private static final long serialVersionUID = 3278334278089660651L;

	public UserException(String message) {
		super(message);
	}
	
	public UserException(String message, Throwable throwable) {
		super(message, throwable);
	}
}