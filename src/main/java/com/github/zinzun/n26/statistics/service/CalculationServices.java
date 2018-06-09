package com.github.zinzun.n26.statistics.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.github.zinzun.n26.statistics.model.Transaction;

@Component
public class CalculationServices implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(CalculationServices.class);
	
	@Value("${app.windowframe}")
    private long windowframe;
	
	//this queue is thread safe and is use to exchange transactions between the service and this thread
	private BlockingQueue<Transaction> queue = new PriorityBlockingQueue<Transaction>();
	
	//This TreeMaps is use to keep the transactions order by timestamp 
	// and it is a List to allow multiple transaction at the same timestamp
	private TreeMap<Long, List<Transaction>> treeMapTransactions = new TreeMap<Long, List<Transaction>>();
	
	//This TreeMaps is use to keep the max and min
	private TreeMap<Double, Integer> treeMapMaxMin = new TreeMap<Double, Integer>();
	
	//Keeps the count of elements 
	private long count=0;

	//Keeps the total of elements 
	private double total=0;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("BlockingQueue ready to recived transactions");
		try {
			while(true){
				//logger.info("Loop");
				long start = System.nanoTime();
				if(!queue.isEmpty()){
					Transaction t = queue.take();
					logger.debug("Processing transaction " + t);
					this.addCount();
					this.addTotal(t);
					this.addTransactionMap(t);
					this.addToMaxMin(t);
				}
				
				this.removeTransactionsOutOfTime();
				


				//logger.info("Stats count:" + this.count + " total:" + this.total + " min:" + this.getMinValue() + " max:" + this.getMaxValue() + " avg:" + this.getAvg());
				//System.out.println(this.treeMapTransactions);
				//System.out.println(this.treeMapMaxMin);
				//long end = System.nanoTime();
				//System.out.println("Total time: " + (end-start));
				
				//Thread.sleep(500);
			}
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	

	

	private void addTransactionMap(Transaction t) {
		List<Transaction> list = this.treeMapTransactions.get(t.getTimestamp());
		if(list == null)
			list = new LinkedList<Transaction>();
		list.add(t);
		this.treeMapTransactions.put(t.getTimestamp(), list);
	}

	private void removeTransactionsOutOfTime() {
		Long windowFrameMillis = System.currentTimeMillis() - windowframe;
		//System.out.println("windowFrameMillis " + windowFrameMillis);
		//Returns all the elements with time stamp expired
		NavigableMap<Long, List<Transaction>> mapRemove = this.treeMapTransactions.headMap(windowFrameMillis, true);
		int totalTransactionsRemove = 0;
		for(List<Transaction> listTransRemove : mapRemove.values()){
			totalTransactionsRemove += listTransRemove.size();
			for(Transaction tran : listTransRemove){
				this.removeToMinMax(tran);
				this.removeTotal(tran);
			}
		}
		if(totalTransactionsRemove > 0)
			this.removeCount(totalTransactionsRemove);
		
		//Remove all the elements at ones
		this.treeMapTransactions.keySet().removeAll(mapRemove.keySet());
	}

	private void addTotal(Transaction t) {
		this.total += t.getAmount();
	}

	private void removeTotal(Transaction t) {
		this.total -= t.getAmount();
	}

	private void removeCount(int totalTransactionsRemove) {
		logger.info("Remove '" + totalTransactionsRemove + "' transactions of the tree");
		this.count -= totalTransactionsRemove;
	}

	private void addCount() {
		this.count++;
	}

	private void addToMaxMin(Transaction t) {
		Integer duplicates = this.treeMapMaxMin.get(t.getAmount());
		this.treeMapMaxMin.put(t.getAmount(), (duplicates == null)?1:++duplicates);
	}
	
	private void removeToMinMax(Transaction t) {
		Integer duplicates = this.treeMapMaxMin.get(t.getAmount());
		if(duplicates > 1)
			this.treeMapMaxMin.put(t.getAmount(), --duplicates);
		else
			this.treeMapMaxMin.remove(t.getAmount());
	}

	public BlockingQueue<Transaction> getQueue(){
		return this.queue;
	}

	public double getMaxValue() {
		if(!this.treeMapMaxMin.isEmpty())
			return this.treeMapMaxMin.lastKey();
		return 0;
	}

	public double getMinValue() {
		if(!this.treeMapMaxMin.isEmpty())
			return this.treeMapMaxMin.firstKey();
		return 0;
	}
	
	public double getTotal() {
		return this.total;
	}
	
	public long getCount(){
		return this.count;
	}
	
	public double getAvg() {
		if(this.count > 0)
			return this.total/this.count;
		return 0;
	}

}
