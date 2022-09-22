package io.springbatch.springbatchlecture.decider;

import java.util.Map;


import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class JobExecutionConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("batchJob")
				.incrementer(new RunIdIncrementer())
				.start(step())
				.next(decider())
				.from(decider()).on("ODD").to(oddStep())
				.from(decider()).on("EVEN").to(evenStep())
				.end()
				.build();
	}
	
	@Bean
	public JobExecutionDecider decider() {
		return new CustomDecider();
	}
	
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
				.tasklet((contribution, chunkContext) ->{
					log.info("----------> step executed");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep")
				.tasklet((contribution, chunkContext) ->{
					log.info("----------> oddstep executed");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep")
				.tasklet((contribution, chunkContext) ->{
					log.info("----------> evenstep executed");
					return RepeatStatus.FINISHED;
				})
				.build();
	}
}
