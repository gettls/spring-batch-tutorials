package io.springbatch.springbatchlecture.chunk;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<Customer>{

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		items.forEach(item -> System.out.println(item));
	}
	
}
