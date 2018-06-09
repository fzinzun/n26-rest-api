package com.github.zinzun.n26.statistics.model;

public class Transaction implements Comparable<Transaction>{
	
	private double amount;
	private long timestamp;
	
	public Transaction() {
		super();
		this.amount = 0;
		this.timestamp = 0;
	}
	
	public Transaction(double amount, long timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString(){
		return "Amount:"+ this.amount + " Milis:" + this.timestamp;
	}

	@Override
	public int compareTo(Transaction o) {
		return (int) (this.getTimestamp() - o.getTimestamp());
	}
}
