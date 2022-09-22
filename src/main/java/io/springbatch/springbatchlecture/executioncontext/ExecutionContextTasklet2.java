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
public class ExecutionContextTasklet2 implements Tasklet{

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("step2 was executed");
		
		ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		
		log.info("jobName : {}", jobExecutionContext.get("jobName"));
		log.info("stepName : {}", stepExecutionContext.get("stepName")); // -> step 간에 공유 X => null
		
		String stepName = chunkContext.getStepContext().getStepExecution().getStepName();
		
		if(stepExecutionContext.get("stepName") == null) {
			stepExecutionContext.put("stepName", stepName);
		}
		
		return RepeatStatus.FINISHED;
	}

	
}
