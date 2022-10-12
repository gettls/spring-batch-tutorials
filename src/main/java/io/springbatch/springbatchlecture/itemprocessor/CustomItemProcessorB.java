package io.springbatch.springbatchlecture.itemprocessor;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorB implements ItemProcessor<ProcessInfo, ProcessInfo>{

	@Override
	public ProcessInfo process(ProcessInfo item) throws Exception {
		System.out.println("CustomItemProcessor B");
		return item;
	}

	
}
