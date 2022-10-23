package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener{

	@Override
	public void beforeRead() {
		System.out.println(">> before read");
	}

	@Override
	public void afterRead(Object item) {
 		System.out.println(">> after read");
	}

	@Override
	public void onReadError(Exception ex) {
		System.out.println(">> on read error");
	}
	
	
}
