package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListener implements StepExecutionListener{

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Step is Starting...");
		System.out.println("Step Name : " + stepExecution.getStepName());
		
		stepExecution.getExecutionContext().put("name", "listener");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		ExitStatus exitStatus = stepExecution.getExitStatus();
		BatchStatus batchStatus = stepExecution.getStatus();
	 
		System.out.println("exitStatus = " + exitStatus);
		System.out.println("batchtatus = " + batchStatus);
		System.out.println((String)stepExecution.getExecutionContext().get("name"));
		
		return ExitStatus.COMPLETED;
	}

	
	
}
