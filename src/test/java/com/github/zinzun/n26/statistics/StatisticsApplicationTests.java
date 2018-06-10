package com.github.zinzun.n26.statistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.zinzun.n26.statistics.service.Calculation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsApplicationTests {
	
	@MockBean
	private Calculation calculationServices;

	@Test
	public void contextLoads() {
	}

}
