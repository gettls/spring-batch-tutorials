package io.springbatch.springbatchlecture.다중처리.eventlistener;

public class CustomSkipException extends RuntimeException{

	public CustomSkipException(String msg) {
		super(msg);
	}
	
	public CustomSkipException(){
		super();
	}
}
