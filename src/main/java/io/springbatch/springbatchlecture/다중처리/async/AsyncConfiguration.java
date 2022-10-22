package io.springbatch.springbatchlecture.다중처리.async;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import io.springbatch.springbatchlecture.itemreader.Customer;
import io.springbatch.springbatchlecture.itemwriter.CustomerRowMapper;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration {

	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	private final DataSource  dataSource;
	
	
	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(step())
				.listener(new StopWatchJobListener())
				.build();
	}
	
	@Bean
	public Step step() throws InterruptedException {
		return stepBuilderFactory.get("step") 
				.<Customer, Customer>chunk(100)
				.reader(pagingItemReader())
				.processor(customItemProcessor())
				.writer(customItemWriter())
				.listener(new StopWatchJobListener())
				.build();
	}
	
	@Bean
	public Step asyncStep() throws InterruptedException {
		return stepBuilderFactory.get("asyncStep") 
				.<Customer, Customer>chunk(100)
				.reader(pagingItemReader())
				.processor(asyncItemProcessor())
				.writer(asyncItemWriter())
				.build();
	}
	
	@Bean
	public AsyncItemWriter asyncItemWriter() {
		AsyncItemWriter<Customer> asyncItemWriter = new AsyncItemWriter<>();
		asyncItemWriter.setDelegate(customItemWriter());
		
		return asyncItemWriter;
	}
	
	@Bean
	public AsyncItemProcessor asyncItemProcessor() throws InterruptedException{
		AsyncItemProcessor<Customer, Customer> asyncItemProcessor = new AsyncItemProcessor<>();
		
		asyncItemProcessor.setDelegate(customItemProcessor());
		asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
		
		return asyncItemProcessor;
	}
	
	@Bean
	public ItemProcessor<Customer, Customer> customItemProcessor() throws InterruptedException{
		return new ItemProcessor<Customer, Customer>() {
			@Override
			public Customer process(Customer item) throws Exception {
				
				Thread.sleep(10L);
				
				return new Customer(item.getId(), item.getName().toUpperCase(), item.getAge()); 
			}
		};
	}
	
	@Bean
	public JdbcPagingItemReader<Customer> pagingItemReader(){
		
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(300);
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
		
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("insert into customer2 values(:id, :name)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		
		return itemWriter;
	}
}
