package io.springbatch.springbatchlecture.다중처리.eventlistener;

public class CustomRetryException extends RuntimeException{

	public CustomRetryException(String msg) {
		super(msg);
	}
	
	public CustomRetryException(){
		super();
	}
}
