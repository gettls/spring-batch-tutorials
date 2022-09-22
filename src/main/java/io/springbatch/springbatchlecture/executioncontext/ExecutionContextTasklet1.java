package io.springbatch.springbatchlecture.executioncontext;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class ExecutionContextTasklet1 implements Tasklet{

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("step1 was executed");
		
		ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();
		
		String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
		String stepName = chunkContext.getStepContext().getStepName();
		
		if(jobExecutionContext.get("jobName") == null) {
			jobExecutionContext.put("jobName", jobName);
		}
		
		if(stepExecutionContext.get("stepName") == null) {
			stepExecutionContext.put("stepName", stepName);
		}
		
		log.info("jobName : {}", jobExecutionContext.get("jobName"));
		log.info("stepName : {}", stepExecutionContext.get("stepName"));
		
		return RepeatStatus.FINISHED;
	}

	
}
