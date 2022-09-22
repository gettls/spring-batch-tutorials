package io.springbatch.springbatchlecture.customexitstatus;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class PassCheckingListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		String exitCode = stepExecution.getExitStatus().getExitCode();
		
		if(!exitCode.equals(ExitStatus.FAILED.getExitCode())) {
			return new ExitStatus("PASS");
		}
		
		return null;
	}
}
