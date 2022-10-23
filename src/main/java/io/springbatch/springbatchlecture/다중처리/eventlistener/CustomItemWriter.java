package io.springbatch.springbatchlecture.다중처리.eventlistener;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CustomItemWriter implements ItemWriter<String>{

	int cnt = 0;
	
	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items) {
			if (cnt < 2) {
				if (cnt % 2 == 0) {
					cnt++;
				} else if(cnt%2 == 1) {
					cnt++;
					throw new CustomRetryException("failed");
				}
			}
			System.out.println("write : " + item);
		}
	}

	
}
