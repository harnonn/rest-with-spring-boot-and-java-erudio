package ca.com.arnon.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongHttpMethodException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public WrongHttpMethodException(String msg) {
		super(msg);
	}
}
