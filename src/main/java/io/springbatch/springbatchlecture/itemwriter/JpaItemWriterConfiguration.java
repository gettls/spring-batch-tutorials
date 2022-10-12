package io.springbatch.springbatchlecture.itemwriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import io.springbatch.springbatchlecture.itemreader.CustomService;
import io.springbatch.springbatchlecture.itemreader.Customer;
import lombok.RequiredArgsConstructor;

//@Configuration
@RequiredArgsConstructor
public class JpaItemWriterConfiguration {

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
				.<Customer, Customer2>chunk(10)
				.reader(customItemReader())
				.processor(customJpaItemProcessor())
				.writer(customItemWriter())
				.build();
	}

	@Bean
	public ItemProcessor<? super Customer, ? extends Customer2> customJpaItemProcessor(){
		return new CustomJpaItemProcessor();
	}
	
	
	@Bean
	public ItemWriter<? super Customer2> customItemWriter(){
		return new JpaItemWriterBuilder<Customer2>()
				.usePersist(true)
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
	
	
	@Bean
	public ItemReader<? extends Customer> customItemReader(){
		
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new CustomerRowMapper());
		
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, age, name");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where name like :name");
		
		Map<String, Order> sortKeys = new HashMap<>(1);
		
		sortKeys.put("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);
		
		HashMap<String, Object> parameters = new HashMap<>();
		parameters.put("name", "A%");
		
		reader.setParameterValues(parameters);
		
		return reader;
	}
	
}
