package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener<Integer, String>{

	@Override
	public void beforeProcess(Integer item) {
		System.out.println(">> before process");
		
	}

	@Override
	public void afterProcess(Integer item, String result) {
		System.out.println(">> after process");
		
	}

	@Override
	public void onProcessError(Integer item, Exception e) {
		System.out.println(">> on process error");
		
	}

	
}
