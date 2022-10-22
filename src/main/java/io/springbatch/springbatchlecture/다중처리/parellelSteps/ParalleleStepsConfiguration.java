package io.springbatch.springbatchlecture.다중처리.parellelSteps;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.springbatch.springbatchlecture.다중처리.async.StopWatchJobListener;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ParalleleStepsConfiguration {
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	
	private final DataSource  dataSource;
	
	@Bean
	public Job job() throws InterruptedException {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(flow1())
				.split()
				.listener(new StopWatchJobListener())
				.build();
	}
	
	@Bean
	public Tasklet tasklet(){
		return new CustomerTasklet();
	}
	
	@Bean
	public Flow flow1() {
		
		TaskletStep step = stepBuilderFactory.get("step1")
				.tasklet(tasklet()).build();
		
		return new FlowBuilder<Flow>("flow1")
				.start(step)
				.build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(8);
		executor.setThreadNamePrefix("async-thread");
		return executor;
	}
}
