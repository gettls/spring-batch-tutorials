package io.springbatch.springbatchlecture.다중처리.parellelSteps;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomerTasklet implements Tasklet {

	private long sum;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		for (int i = 0; i < 1000000; i++) {
			sum++;
		}

		System.out.println(String.format("%s has been executed on thread %s", 
				chunkContext.getStepContext().getStepName(), Thread.currentThread().getName()));
		
		return RepeatStatus.FINISHED;
	}

}
