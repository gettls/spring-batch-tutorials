package io.springbatch.springbatchlecture.다중처리.eventlistener;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.springbatch.springbatchlecture.itemreader.Customer;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ListenerConfiguration {
	
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	private final CustomStepExecutionListener customStepExecutionListener;
	
	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Integer, String>chunk(10)
				.listener(new CustomChunkListener())
				.listener(new CustomItemReadListener())
				.listener(new CustomItemProcessListener())
				.listener(new CustomItemWriteListener())
				.reader(listItemReader())
				.writer((ItemWriter<String>) items -> {
					System.out.println("itmes = " + items);
				})
				.build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
				.build();
	}
	
	@Bean
	public ItemReader<Integer> listItemReader(){
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		return new ListItemReader<>(list);
	}
	
}
