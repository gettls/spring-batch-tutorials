package io.springbatch.springbatchlecture.itemprocessor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springbatch.springbatchlecture.itemreader.CustomService;
import lombok.RequiredArgsConstructor;

//@Configuration
@RequiredArgsConstructor
public class CompositeItemProcessorConfiguration {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final DataSource dataSource; 
	
	@Bean
	public Job batchJob() throws Exception {
		return this.jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
	
	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(10)
				.reader(new ItemReader<String>() {
					@Override
					public String read()
							throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
						int i=0;
						return i > 10 ? null : "item" + i;
					}
				})
				.processor(customItemProcessor())
				.writer(items -> System.out.println(items))
				.build();
	}
	
	@Bean
	public ItemProcessor<String, String> customItemProcessor(){
		List itemProcessors = new ArrayList<>();
		itemProcessors.add(new CustomItemProcessor1());
		itemProcessors.add(new CustomItemProcessor2());
		
		return new CompositeItemProcessorBuilder<>()
				.delegates(itemProcessors)
				.build();
	}
}