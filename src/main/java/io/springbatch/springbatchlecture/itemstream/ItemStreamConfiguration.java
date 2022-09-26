package io.springbatch.springbatchlecture.itemstream;

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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ItemStreamConfiguration {

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
				.writer(itemWriter())
				.build();
	}
	
	@Bean
	public ItemWriter<? super String> itemWriter(){
		return new CustomItemWriter();
	}
	
	public CustomItemStreamReader itemReader() {
		List<String> items = new ArrayList<>(10);
		for(int i=0;i<10;i++) {
			items.add(String.valueOf(i));
		}
		return new CustomItemStreamReader(items);
		
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
