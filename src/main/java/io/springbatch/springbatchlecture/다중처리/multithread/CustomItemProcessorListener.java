package io.springbatch.springbatchlecture.다중처리.multithread;

import org.springframework.batch.core.ItemProcessListener;

import io.springbatch.springbatchlecture.itemreader.Customer;

public class CustomItemProcessorListener implements ItemProcessListener<Customer, Customer>{

	@Override
	public void beforeProcess(Customer item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterProcess(Customer item, Customer result) {
		System.out.println("Thread : " + Thread.currentThread() + "process item : " + item.getId());
		
	}

	@Override
	public void onProcessError(Customer item, Exception e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
