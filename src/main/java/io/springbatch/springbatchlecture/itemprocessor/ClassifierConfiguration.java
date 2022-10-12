package io.springbatch.springbatchlecture.itemprocessor;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ClassifierConfiguration {

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
				.<ProcessInfo, ProcessInfo>chunk(10)
				.reader(new ItemReader<ProcessInfo>() {
					int i=0;
					@Override
					public ProcessInfo read()
							throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
						i++;
						ProcessInfo processInfo = ProcessInfo.builder().id(i).build();
						
						return i > 3? null : processInfo;
					}
				})
				.processor(customItemProcessor())
				.writer(items -> System.out.println(items))
				.build();
	}

	@Bean
	public ItemProcessor<? super ProcessInfo, ? extends ProcessInfo> customItemProcessor(){

		Map<Integer, ItemProcessor<ProcessInfo, ProcessInfo>> processorMap = new HashMap<>();
		processorMap.put(1, new CustomItemProcessorA());
		processorMap.put(2, new CustomItemProcessorB());
		processorMap.put(3, new CustomItemProcessorC());
		
		ProcessorClassifier<ProcessInfo, ItemProcessor<?, ? extends ProcessInfo>> classifier = new ProcessorClassifier<>();
		classifier.setProcessorMap(processorMap);
		
		ClassifierCompositeItemProcessor<ProcessInfo, ProcessInfo> processor = new ClassifierCompositeItemProcessor<>();
		processor.setClassifier(classifier);
		
		return processor;
	}
	
}
