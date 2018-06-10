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
/***
 * This service is use by the controller and checks if the transactions time stamp is inside the time frame.  
 * @author Francisco Zinzun
 *
 */
@Service
public class StatisticsService {
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);
	
	/***
	 * This property is the time frame in milliseconds 
	 */
	@Value("${app.windowframe}")
    private long windowframe;
	
	@Autowired
	Calculation calculation;

	public String processTransaction(Transaction transaction) {
		long windowFrameMillis = System.currentTimeMillis() - windowframe;

    	if(transaction.getTimestamp() < windowFrameMillis){ 
    		logger.debug("BlockingQueue ready to recived transactions");
    		throw new OutOfRangeException();
    	}
    	
    	try {
			calculation.getQueue().put(transaction);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null; //return empty body
	}

	public StatisticResponse getStatistics() {
    	StatisticResponse stat = new StatisticResponse( calculation.getTotal(),
										    			calculation.getAvg(),
										    			calculation.getMaxValue(),
										    			calculation.getMinValue(),
										    			calculation.getCount());
		return stat;
	}

}
