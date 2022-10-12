package io.springbatch.springbatchlecture.itemwriter;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import io.springbatch.springbatchlecture.itemreader.CustomService;
import io.springbatch.springbatchlecture.itemreader.Customer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FlatFilesFormattedConfiguration {

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
				.<Customer, Customer>chunk(10)
				.reader(customItemReader())
				.writer(customItemWriter())
				.build();
	}

	@Bean
	public ItemWriter<? super Customer> customItemWriter(){
		return new FlatFileItemWriterBuilder<>()
				.name("flatFileWriter")
				.resource(new FileSystemResource("./src/main/resources/customer.txt"))
				.delimited()
				.delimiter("|")
				.names(new String[] {"id","age","name"})
				.build();
	}
	
	
	@Bean
	public ItemReader<? extends Customer> customItemReader(){
		List<Customer> customers = Arrays.asList(new Customer(1, "홍길동", 41),
				new Customer(1, "김동구", 15),
				new Customer(1, "박장주", 20));
		
		ListItemReader<Customer> reader = new ListItemReader<>(customers);
		
		return reader;
	}
	
}
