package io.springbatch.springbatchlecture.itemwriter;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Customer2 {

	@Id
	private long id;
	private String name;
	private int age;
	
}
