package io.springbatch.springbatchlecture.batchtest;


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springbatch.springbatchlecture.itemreader.Customer;
import io.springbatch.springbatchlecture.itemwriter.CustomerRowMapper;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobOperationConfiguration {

	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final JobRegistry jobRegistry;  // jobRegistry 는 시작과 동시에 생성

	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.next(step2())
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("stpe1 was executed");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("stpe1 was executed");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	
	@Bean
	public BeanPostProcessor jobRegistryBeanPostProcessor() {
		JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
		beanPostProcessor.setJobRegistry(jobRegistry);
		return beanPostProcessor;
	}
}