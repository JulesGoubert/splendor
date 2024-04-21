package exceptions;

import domein.SpelerRepository;

public class TeVeelSpelersException extends IllegalArgumentException {
	
	public TeVeelSpelersException() {
		super(String.format("maxSpelersError", 
				  SpelerRepository.MAX_AANTAL_SPELERS));
	}
	
	public TeVeelSpelersException(String msg) {
		super(msg);
	}
	
	public TeVeelSpelersException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public TeVeelSpelersException(Throwable cause) {
		super(cause);
	}
	
}
