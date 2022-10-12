package io.springbatch.springbatchlecture.itemwriter;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import io.springbatch.springbatchlecture.itemreader.Customer;

public class CustomJpaItemProcessor implements ItemProcessor<Customer, Customer2>{

	ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public Customer2 process(Customer item) throws Exception {
		Customer2 customer2 = modelMapper.map(item, Customer2.class);
		return customer2;
	}

}
