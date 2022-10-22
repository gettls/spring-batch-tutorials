package io.springbatch.springbatchlecture.다중처리.multithread;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

import io.springbatch.springbatchlecture.itemreader.Customer;

public class CustomItemWriterListener implements ItemWriteListener<Customer>{

	@Override
	public void beforeWrite(List<? extends Customer> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterWrite(List<? extends Customer> items) {
		System.out.println("Thread : " + Thread.currentThread() + "write item : " + items.size());
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Customer> items) {
		// TODO Auto-generated method stub
		
	}

	
	
}
