package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class CustomAnnotationJobExecutionListener {

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job is Starting...");
		System.out.println("Job Name : " +jobExecution.getJobInstance().getJobName());
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Start Time : " + jobExecution.getStartTime());
		System.out.println("End Time : " + jobExecution.getEndTime());
	}
	
}
