package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Integer, String> {

	int cnt = 0;

	@Override
	public String process(Integer item) throws Exception {

		if (cnt < 2) {
			if (cnt % 2 == 0) {
				cnt++;
			} else if(cnt%2 == 1) {
				cnt++;
				throw new CustomRetryException("failed");
			}
		}
		
		return String.valueOf(item);
	}

}
