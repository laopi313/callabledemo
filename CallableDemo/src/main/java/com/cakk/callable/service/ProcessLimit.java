package com.cakk.callable.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

public class ProcessLimit implements Callable<Integer>{
	int number;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public ProcessLimit(int number) {
		this.number = number;
	}
	@Override
	public Integer call() throws Exception {
		int processTime = ThreadLocalRandom.current().nextInt(10, 30 + 1);
		System.out.println("Processing:"+number+", process time:"+processTime);
		Thread.sleep(processTime*1000);
		return number;
	}
}