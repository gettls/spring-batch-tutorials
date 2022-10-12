package io.springbatch.springbatchlecture.반복및오류제어;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String>{

	private int cnt = 0 ;
	
	@Override
	public String process(String item) throws Exception {
		
		if(item.equals("6") || item.equals("7")) {
			throw new SkippableException("Process failed cnt : " + cnt);
		}
		else {
			System.out.println("itemProcessor : " + item);
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}
}
