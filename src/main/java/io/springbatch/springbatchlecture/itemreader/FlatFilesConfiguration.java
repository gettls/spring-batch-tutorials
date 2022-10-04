package io.springbatch.springbatchlecture.itemreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import lombok.RequiredArgsConstructor;

//@Configuration
@RequiredArgsConstructor
public class FlatFilesConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job batchJob() {
		return this.jobBuilderFactory.get("batchJob")
				.start(step1())
				.next(step2())
				.build();
	}
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<String, String>chunk(5)
				.reader(itemReader())
				.writer(null)
				.build();
	}
	
//	@Bean
//	public ItemReader itemReader() {
//		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//		itemReader.setResource(new ClassPathResource("/customer.csv"));
//		
//		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//		lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
//		lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
//		
//		itemReader.setLineMapper(lineMapper);
//		itemReader.setLinesToSkip(1);
//		
//		return itemReader;
//	}
	
//	@Bean
//	public ItemReader itemReader() {
//		return new FlatFileItemReaderBuilder<Customer>()
//				.name("flatFile")
//				.resource(new ClassPathResource("/customer.csv"))
//				.fieldSetMapper(new CustomerFieldSetMapper())
//				.linesToSkip(1)
//				.delimited().delimiter(",")
//				.names("name","age","year")
//				.build();
//	}
	
	public FlatFileItemReader itemReader() {
		return new FlatFileItemReaderBuilder()
				.name("flatFile")
				.resource(new FileSystemResource("/customer.txt"))
				.fieldSetMapper(new BeanWrapperFieldSetMapper<>())
				.targetType(Customer.class)
				.linesToSkip(1)
				.fixedLength()
				.addColumns(new Range(1, 5))
				.addColumns(new Range(6, 9))
				.addColumns(new Range(10, 11))
				.names("name","year","age")
				.build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet(new Tasklet() {
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("step2 was executed");
						return RepeatStatus.FINISHED;
					}
				})
				.build();
	}
	
}
