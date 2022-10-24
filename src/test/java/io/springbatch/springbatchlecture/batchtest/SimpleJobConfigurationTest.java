package io.springbatch.springbatchlecture.batchtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBatchTest
@SpringBootTest(classes = {SimpleJobConfigurationTest.class, TestBatchConfig.class})
class SimpleJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@AfterEach
	public void clear() {
		jdbcTemplate.execute("delete from customer2");
	}
	
	@Test
	void simpleJob_test() throws Exception{
		
		// given
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("name", "user1")
				.addLong("data", new Date().getTime())
				.toJobParameters();
		
		// when
//		JobExecution jobExecution =	jobLauncherTestUtils.launchJob(jobParameters);
		JobExecution jobExecution1 = jobLauncherTestUtils.launchStep("step1");
		
		// then
//		assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
//		assertEquals(jobExecution.getExitStatus(), BatchStatus.COMPLETED);
		
		StepExecution stepExecution = (StepExecution)((List)jobExecution1.getStepExecutions()).get(0);
		
		assertEquals(stepExecution.getCommitCount(), 11); // itemReader 1001 번째 data가 있는지 확인하기 위한 commit 추가 발생 
		assertEquals(stepExecution.getReadCount(), 1000);
		assertEquals(stepExecution.getWriteCount(), 1000);
		
	}
}