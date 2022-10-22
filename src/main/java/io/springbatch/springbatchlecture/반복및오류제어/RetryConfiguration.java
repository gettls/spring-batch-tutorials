package io.springbatch.springbatchlecture.반복및오류제어;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import io.springbatch.springbatchlecture.itemreader.Customer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {

	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(step())
				.build();
	}
	
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step") 
				.<String,Customer>chunk(5)
				.reader(reader())
				.processor(processor2())
				.writer(items -> items.forEach(item -> System.out.println(item)))
				.faultTolerant()
				.skip(RetryableException.class)
				.skipLimit(2)
//				.retry(RetryableException.class)
//				.retryLimit(2)
//				.retryPolicy(retryPolicy())
				.build();
	}
	
//	@Bean
//	public ItemProcessor<? super String, String> processor(){
//		return new RetryItemProcessor();
//	}
	
	@Bean
	public ListItemReader<String> reader(){
		List<String> items = new ArrayList<>();
		for(int i=0;i<30;i++) {
			items.add(String.valueOf(i));
		}
		return new ListItemReader<>(items);
	}
	
	@Bean
	public ItemProcessor<String, Customer> processor2(){
		return new RetryItemProcessor2();
	}
	
//	@Bean
//	public RetryPolicy retryPolicy() {
//		Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
//		exceptionClass.put(RetryableException.class, true);
//		
//		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);
//		
//		return simpleRetryPolicy;
//	}
	
	@Bean
	public RetryTemplate retryTemplate() {
		Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
		exceptionClass.put(RetryableException.class, true);
		
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(2000);
		
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);
		
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(simpleRetryPolicy);
//		retryTemplate.setBackOffPolicy(backOffPolicy);
		
		return retryTemplate;
	}
}
