package com.github.zinzun.n26.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.zinzun.n26.statistics.model.StatisticResponse;
import com.github.zinzun.n26.statistics.model.Transaction;
import com.github.zinzun.n26.statistics.service.StatisticsService;
/***
 * This class expose the REST endpoints '/transactions' and '/statistics' which are the unique two endpoints. 
 * @author Francisco Zinzun
 *
 */
@RestController
@RequestMapping("/")
public class StatisticsController {
	
	@Autowired
	StatisticsService statService;

    @PostMapping(value="transactions")
    @ResponseStatus( HttpStatus.CREATED) //status 201
    public String transactions(@RequestBody Transaction transaction) {
        return statService.processTransaction(transaction);
    }
    
    @GetMapping(value="statistics")
    public StatisticResponse statistics() {
        return statService.getStatistics();
    }
	
}
