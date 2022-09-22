package io.springbatch.springbatchlecture.increment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.SimpleJobRepository;

public class CustomJobParametersIncrementer implements JobParametersIncrementer{

	static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd--hhmmss");
	
	@Override
	public JobParameters getNext(JobParameters parameters) {
		
		String id = format.format(new Date());
		
		return new JobParametersBuilder().addString("run.id", id).toJobParameters();
	}
}
