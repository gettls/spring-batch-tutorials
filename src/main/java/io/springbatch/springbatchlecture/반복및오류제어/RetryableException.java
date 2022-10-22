package io.springbatch.springbatchlecture.반복및오류제어;

import org.springframework.retry.RetryException;

public class RetryableException extends RuntimeException {

	public RetryableException() {
		super();
	}
	
	public RetryableException(String msg) {
		super(msg);
	}
}
