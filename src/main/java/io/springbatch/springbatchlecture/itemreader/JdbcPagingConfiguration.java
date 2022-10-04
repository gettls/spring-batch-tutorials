package io.springbatch.springbatchlecture.itemreader;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JdbcPagingConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource; 
	
	@Bean
	public Job batchJob() throws Exception {
		return this.jobBuilderFactory.get("batchJob")
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
	public ItemReader<? extends Customer> customItemReader() throws Exception{
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("firstName", "A%");

		return new JdbcPagingItemReaderBuilder<Customer>()
				.name("jdbcPagingReader")
				.pageSize(10)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Customer.class))
				.queryProvider(createQueryProvider())
				.parameterValues(parameters)
				.build();
	}
	
	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
		
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, firstName, lastName, birthDate");
		queryProvider.setFromClause("from customer");
		queryProvider.setWhereClause("where firstname like :firstname");
		
		
		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);
		
		queryProvider.setSortKeys(sortKeys);
		
		return queryProvider.getObject();
	}
	
	@Bean
	public ItemWriter<Customer> customItemWriter(){
		return items -> {
			for(Customer item : items){			
				System.out.println(item.toString());
			}
		};
	}
}
