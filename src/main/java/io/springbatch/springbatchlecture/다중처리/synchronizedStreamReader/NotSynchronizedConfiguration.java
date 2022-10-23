package io.springbatch.springbatchlecture.다중처리.synchronizedStreamReader;

import javax.sql.DataSource;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.springbatch.springbatchlecture.itemreader.Customer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class NotSynchronizedConfiguration {
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	private final DataSource  dataSource;
	
	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("masterStep")
				.<Customer, Customer>chunk(60)
				.reader(customItemReader())
				.listener(new ItemReadListener<Customer>() {
					@Override
					public void beforeRead() {
					}
					@Override
					public void afterRead(Customer item) {
						System.out.println("item.getId() : " + item.getId());
					}
					@Override
					public void onReadError(Exception ex) {
					}
				})
				.writer(customItemWriter())
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(8);
		taskExecutor.setThreadNamePrefix("not-safety-thread");
		return taskExecutor;
	}
	
	@Bean
	@StepScope
	public SynchronizedItemStreamReader<Customer> customItemReader() {
		
		JdbcCursorItemReader<Customer> notSafetyReader = new JdbcCursorItemReaderBuilder<Customer>()
				.fetchSize(60)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Customer.class))
				.sql("select id, name from customer")
				.name("NotSafetyReader")
				.build(); 
		
//		return new JdbcCursorItemReaderBuilder<Customer>()
//				.fetchSize(60)
//				.dataSource(dataSource)
//				.rowMapper(new BeanPropertyRowMapper<>(Customer.class))
//				.sql("select id, name from customer")
//				.name("NotSafetyReader")
//				.build();
		return new SynchronizedItemStreamReaderBuilder<Customer>()
				.delegate(notSafetyReader)
				.build();
	}
	
	@Bean
	@StepScope
	public JdbcBatchItemWriter customItemWriter() {
		JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
		
		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("insert into customer2 values(:id, :name)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		
		return itemWriter;
	}
}
