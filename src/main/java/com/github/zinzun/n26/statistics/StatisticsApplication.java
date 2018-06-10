package com.github.zinzun.n26.statistics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
/***
 * This is the main entry point for the application. 
 * @author Francisco Zinzun
 *
 */
@SpringBootApplication
public class StatisticsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}
	
}
