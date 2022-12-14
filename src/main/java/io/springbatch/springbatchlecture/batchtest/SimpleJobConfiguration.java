package io.springbatch.springbatchlecture.batchtest;


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.springbatch.springbatchlecture.itemreader.Customer;
import io.springbatch.springbatchlecture.itemwriter.CustomerRowMapper;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SimpleJobConfiguration {

	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final DataSource dataSource;
	

	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Customer, Customer>chunk(100)
				.reader(customItemReader())
				.writer(customItemWriter())
				.build();
	}

	@Bean
	public JdbcPagingItemReader<Customer> customItemReader(){ 
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader();
		
		reader.setDataSource(dataSource);
		reader.setPageSize(100);
		reader.setRowMapper(new CustomerRowMapper());
		
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, name");
		queryProvider.setFromClause("from customer");
		
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		
		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);
		
		return reader;
	}
	
	@Bean
	public JdbcBatchItemWriter customItemWriter() {
		JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
		
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into customer2 values (:id, :name)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		
		return itemWriter;
	}
}