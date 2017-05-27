package com.prakash.shopapi.io.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NullValueException extends RuntimeException {

	private static final long serialVersionUID = 2796552782164782717L;

	public NullValueException() {
		super();
	}

	public NullValueException(String errorMsg) {
		super(errorMsg);
	}

	public NullValueException(String errorMsg, Throwable cause) {
		super(errorMsg, cause);
	}

	public NullValueException(Throwable cause) {
		super(cause);
	}
}
