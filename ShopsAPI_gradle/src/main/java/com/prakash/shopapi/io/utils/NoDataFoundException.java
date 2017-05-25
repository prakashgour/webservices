package com.prakash.shopapi.io.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoDataFoundException extends RuntimeException {
	private static final long serialVersionUID = -3866842046845921983L;
	
	public NoDataFoundException() {
		super();
	}

	public NoDataFoundException(String errorMsg) {
		super(errorMsg);
	}
	
	public NoDataFoundException(Throwable cause) {
		super(cause);
	}
	
	public NoDataFoundException(String errorMsg, Throwable cause) {
		super(errorMsg, cause);
	}
}
