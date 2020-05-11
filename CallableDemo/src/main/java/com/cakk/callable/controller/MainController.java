package com.cakk.callable.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cakk.callable.util.BatchService;
import com.cakk.callable.util.ServiceConstants;

@RestController
public class MainController {
	@GetMapping("/pause")
	public void pauseProcess() {
		System.out.println("Received PAUSE command.");
	}
	@GetMapping("/stop")
	public void stopProcess() {
		System.out.println("Received STOP command.");
	}
	@GetMapping("/resume")
	public void resumeProcess() {
		System.out.println("Received RESUME command.");
	}
	@GetMapping("/start")
	public void startProcess() {
		System.out.println("Received START command.");
		try {
			BatchService.process();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
