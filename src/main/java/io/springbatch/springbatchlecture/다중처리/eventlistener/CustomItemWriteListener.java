package io.springbatch.springbatchlecture.다중처리.eventlistener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.ItemWriter;

public class CustomItemWriteListener implements ItemWriteListener<String>{

	@Override
	public void beforeWrite(List<? extends String> items) {
		System.out.println(">> before write");
		
	}

	@Override
	public void afterWrite(List<? extends String> items) {
		System.out.println(">> after write");
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends String> items) {
		System.out.println(">> on write error");
	}

	
	
	
}
