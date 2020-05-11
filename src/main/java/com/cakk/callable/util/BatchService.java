package com.cakk.callable.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.cakk.callable.util.ServiceConstants;
import com.cakk.callable.model.Limit;
import com.cakk.callable.service.ProcessLimit;

public final class BatchService {
	public static void process() throws InterruptedException, ExecutionException {
		int limitNumber = 0;
		List<Limit> totalList = generateProcessList();
		List<Limit> processedList = new ArrayList<>();

		List<Limit> processingList = new ArrayList<>();
		ExecutorService service = Executors.newFixedThreadPool(ServiceConstants.THREADS);
		//submit 10 to pool first
		List<Future<Integer>> results = new ArrayList<>();
		for(int i = 0; i < ServiceConstants.THREADS; i++) {	//this result will block here
			limitNumber++;
			System.out.println("Submit:"+i);
			results.add(service.submit(new ProcessLimit(i)));
			processingList.add(new Limit(i, false));		//list in thread pool
			
		}
		while(limitNumber < ServiceConstants.LIMIT_NUMBER || processingList.size() > 0) {
			List<Future<Integer>> doneList = results.stream()
				.filter(r->r.isDone())
				.collect(Collectors.toList());
				
			for(Future<Integer> r: doneList) {
				AtomicInteger ai = new AtomicInteger(0);
				try {
					ai.set(r.get());
					System.out.println("Done:"+ai.get());
					//mark process true
					//System.out.println("Before. processed:");
					//printLimitList(processedList);
					processedList.addAll(totalList.stream()
						.filter(t->ai.get() == t.getItem())
						.collect(Collectors.toList()));
					//System.out.println("After. processed:");
					//printLimitList(processedList);
					//System.out.println("Before. processing:");
					//printLimitList(processingList);
					processingList = processingList.stream()
							.filter(t->ai.get() != t.getItem())
							.collect(Collectors.toList());
					//System.out.println("After. processing:");
					//printLimitList(processingList);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				//System.out.println("processingList:"+processingList.size());
				results.remove(r);
				if(ServiceConstants.COMMAND == ServiceConstants.COMMAND_STOP && processingList.size() == 0) {
					System.out.println("Received STOP command, all items on the list processed.");
					break;
				}
				if(ServiceConstants.COMMAND == ServiceConstants.COMMAND_START || 
					ServiceConstants.COMMAND == ServiceConstants.COMMAND_RESUME ) {
					if(limitNumber < ServiceConstants.LIMIT_NUMBER) {
						processingList.add(totalList.get(limitNumber));
						results.add(service.submit(new ProcessLimit(totalList.get(limitNumber++).getItem())));
					}
				}
			}
		}
		System.out.println("All process done!");
	}
	private static List<Limit> generateProcessList() {
		return IntStream.rangeClosed(0,19).boxed().map(i->new Limit(i, false)).collect(Collectors.toList());
	}
	
	private static void printLimitList(List<Limit> list) {
		list.stream().forEach(l->System.out.println(l.getItem()+","));
	}
}
