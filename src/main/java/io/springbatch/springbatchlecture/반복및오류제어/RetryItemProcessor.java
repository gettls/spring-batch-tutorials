package io.springbatch.springbatchlecture.반복및오류제어;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

	private int cnt = 0;

	@Override
	public String process(String item) throws Exception {
		if (item.equals("2") || item.equals("3")) {
			cnt++;
			throw new RetryableException();
		}
		return item;
	}

}
