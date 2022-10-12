package io.springbatch.springbatchlecture.itemprocessor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

public class TESTProcessorClassifier<C, T> implements Classifier<C, T> {

	private Map<Integer, ItemProcessor<String, String>> processorMap = new HashMap<>();
	
	@Override
	public T classify(C classifiable) {
		return (T)processorMap.get(((ProcessInfo)classifiable).getId());
	}

	public void setProcessorMap(Map<Integer, ItemProcessor<String, String>> processorMap) {
		this.processorMap = processorMap;
	}
}
