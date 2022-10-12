package io.springbatch.springbatchlecture.itemprocessor;

import org.springframework.batch.item.ItemProcessor;

public class TESTProcessorA implements ItemProcessor<String, String>{

	@Override
	public String process(String item) throws Exception {
		System.out.println("CustomItemProcessor A");
		return item;
	}

	
}
