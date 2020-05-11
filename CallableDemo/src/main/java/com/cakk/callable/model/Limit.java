package com.cakk.callable.model;

public class Limit {
	private int item;
	private boolean process;
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public boolean isProcess() {
		return process;
	}
	public void setProcess(boolean process) {
		this.process = process;
	}
	public Limit(int item, boolean process) {
		super();
		this.item = item;
		this.process = process;
	}
	
}
	
