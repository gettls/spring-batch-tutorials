package io.springbatch.springbatchlecture.반복및오류제어;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

public class SkipItemWriter implements ItemWriter<String> {

	private int cnt = 0;

	@Override
	public void write(List<? extends String> items) throws Exception {
		for (String item : items) {
			if (item.equals("-12")) {
				throw new SkippableException("Process failed cnt : " + cnt);
			} else {
				System.out.println("itemWriter : " + item);
			}
		}
	}
}
