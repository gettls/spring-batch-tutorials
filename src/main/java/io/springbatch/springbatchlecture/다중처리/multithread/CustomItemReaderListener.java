package io.springbatch.springbatchlecture.다중처리.multithread;

import org.springframework.batch.core.ItemReadListener;

import io.springbatch.springbatchlecture.itemreader.Customer;

public class CustomItemReaderListener implements ItemReadListener<Customer>{

	@Override
	public void beforeRead() {
		
	}

	@Override
	public void afterRead(Customer item) {
		System.out.println("Thread : " + Thread.currentThread() + "read item : " + item.getId());
	}

	@Override
	public void onReadError(Exception ex) {
		
	}
}
