package io.springbatch.springbatchlecture.scope;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobScope_StepScope_Configuration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job flowJob() {
		return jobBuilderFactory.get("batchJob")
				.start(step1(null))
				.next(step2())
				.listener(new JobListener())
				.build();
	}
	
	@Bean
	@JobScope
	public Step step1(@Value("#{jobParameters['message']}") String message) {
		System.out.println("message = " + message);
		return stepBuilderFactory.get("step1")
				.tasklet(tasklet1(null))
				.build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet(tasklet2(null))
				.listener(new CustomStepListener())
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet tasklet1(@Value("#{jobExecutionContext['name']}") String name) {
		System.out.println("name1 = " + name);
		return (stepContribution, chunkContext) -> {
			System.out.println("tasklet1 has executed");
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	@StepScope
	public Tasklet tasklet2(@Value("#{jobExecutionContext['name']}") String name) {
		System.out.println("name2 = " + name);
		return (stepContribution, chunkContext) -> {
			System.out.println("tasklet2 has executed");
			return RepeatStatus.FINISHED;
		};
	}
}
