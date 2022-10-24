package io.springbatch.springbatchlecture.batchtest;

import java.util.Iterator;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

	@Autowired
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobExplorer  jobExplorer;
	
	@Autowired
	private JobOperator jobOperator;
	
	@PostMapping("/batch/start")
	public String start(@RequestBody JobInfo jobInfo) throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
		
		for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
			SimpleJob job = (SimpleJob)jobRegistry.getJob(iterator.next());
			System.out.println("jobName : " + job.getName());
			jobOperator.start(job.getName(), "id="+jobInfo.getId());
		}
		
		return "batch is started";
	}
	
	@PostMapping("/batch/stop")
	public String stop() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException, NoSuchJobExecutionException, JobExecutionNotRunningException {
		
		for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
			SimpleJob job = (SimpleJob)jobRegistry.getJob(iterator.next());
			System.out.println("jobName : " + job.getName());
			
			Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions(job.getName());
			JobExecution jobExecution = runningJobExecutions.iterator().next();
			
			jobOperator.stop(jobExecution.getId());
		}
		
		return "batch is stopped";
	}
	
	/*
	 * 재시작이 되는 조건
	 * -> Job 이 실패하는 경우
	 */
	@PostMapping("/batch/restart")
	public String restart() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException, NoSuchJobExecutionException, JobExecutionNotRunningException, JobInstanceAlreadyCompleteException, JobRestartException {
		
		for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
			SimpleJob job = (SimpleJob)jobRegistry.getJob(iterator.next());
			System.out.println("jobName : " + job.getName());

			/*
			 * 마지막을 가져오는 이유
			 * -> 마지막에 남아있는 JobInstance가 실패한 JobInstance 이기 때문임
			 */
			JobInstance jobInstance = jobExplorer.getLastJobInstance(job.getName());
			JobExecution lastJobExecution = jobExplorer.getLastJobExecution(jobInstance);
			
			jobOperator.restart(lastJobExecution.getId());
		}
		
		return "batch is restarted";
	}
	
}
