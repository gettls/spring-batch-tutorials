package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Integer, String>{

	@Override
	public void onSkipInRead(Throwable t) {
		System.out.println(">> on skip read : " + t.getMessage());
	}

	@Override
	public void onSkipInWrite(String item, Throwable t) {
		System.out.println(">> on skip write : " + item);
		System.out.println(">> on skip write : " + t.getMessage());
	}

	@Override
	public void onSkipInProcess(Integer item, Throwable t) {
		System.out.println(">> on skip process : " + item);
		System.out.println(">> on skip process : " + t.getMessage());
	}

	
	
}
