package io.springbatch.springbatchlecture.반복및오류제어;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

//@Configuration
@RequiredArgsConstructor
public class SkipConfiguration {

	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.start(step1())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<String,String>chunk(5)
				.reader(new ItemReader<String>() {
					int i = 0;
					@Override
					public String read()
							throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
						i++;
						
						if(i==3) {
							throw new SkippableException("skip");
						}
						
						return i > 20 ? null : "item" + i;
					}
				})
				.processor(itemProcessor())
				.writer(itemWriter())
				.faultTolerant()
				.skipPolicy(itemCheckingItemSkipPolicy())
//				.skip(SkippableException.class)
//				.skipLimit(2)
				.build();
	}
	
	@Bean
	public SkipPolicy itemCheckingItemSkipPolicy() {
		Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
		exceptionClass.put(SkippableException.class, true);
		
		LimitCheckingItemSkipPolicy checkingItemSkipPolicy = new  LimitCheckingItemSkipPolicy(3, exceptionClass);
		return checkingItemSkipPolicy;
	}
	
	@Bean
	public ItemProcessor<String, String> itemProcessor(){
		return new SkipItemProcessor();
	}
	
	@Bean
	public ItemWriter<String> itemWriter(){
		return new SkipItemWriter();
	}
	
}
