package io.springbatch.springbatchlecture.다중처리.eventlistener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

public class CustomChunkListener {

	@BeforeChunk
	public void beforeChunk(ChunkContext chunkContext) {
		System.out.println(">> before chunk");
	}
	
	@AfterChunk
	public void afterChunk(ChunkContext chunkContext) {
		System.out.println(">> after chunk");
	}
	
	@AfterChunkError
	public void afterChunkError(ChunkContext chunkContext) {
		System.out.println(">> after chunk error");
	}
	
}
