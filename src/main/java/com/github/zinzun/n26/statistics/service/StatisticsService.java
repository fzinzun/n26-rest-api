package com.github.zinzun.n26.statistics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.zinzun.n26.statistics.model.StatisticResponse;
import com.github.zinzun.n26.statistics.model.Transaction;
import com.github.zinzun.n26.statistics.utils.OutOfRangeException;

@Service
public class StatisticsService {
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

	@Value("${app.windowframe}")
    private long windowframe;
	
	@Autowired
	Calculation calculationServices;

	public String processTransaction(Transaction transaction) {
		long windowFrameMillis = System.currentTimeMillis() - windowframe;
    	/*System.out.println("currentTimeMillis: " + System.currentTimeMillis());
    	System.out.println("transaction:       " + transaction.getTimestamp());
    	System.out.println("windowFrameMillis: " + windowFrameMillis);
    	System.out.println("-----------------: " + (transaction.getTimestamp() < windowFrameMillis));*/
    	if(transaction.getTimestamp() < windowFrameMillis){ 
    		logger.debug("BlockingQueue ready to recived transactions");
    		throw new OutOfRangeException();
    	}
    	
    	try {
			calculationServices.getQueue().put(transaction);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null; //return empty body
	}

	public StatisticResponse getStatistics() {
    	StatisticResponse stat = new StatisticResponse( calculationServices.getTotal(),
										    			calculationServices.getAvg(),
										    			calculationServices.getMaxValue(),
										    			calculationServices.getMinValue(),
										    			calculationServices.getCount());
		return stat;
	}

}
