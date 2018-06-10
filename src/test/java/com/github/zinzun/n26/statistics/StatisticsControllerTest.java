package com.github.zinzun.n26.statistics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zinzun.n26.statistics.model.Transaction;
import com.github.zinzun.n26.statistics.service.Calculation;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = StatisticsApplication.class)
//@WebAppConfiguration
@AutoConfigureMockMvc
public class StatisticsControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	@Value("${app.windowframe}")
    private long windowframe;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private Calculation calculationServices;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void addTransactions() throws Exception{
		when(calculationServices.getQueue()).thenReturn(new PriorityBlockingQueue<Transaction>());
		mockMvc.perform(post("/transactions") 
                .content(mapper.writeValueAsString(new Transaction(12.3, System.currentTimeMillis())))
                .contentType(contentType))
                .andExpect(status().isCreated());
	}
	
	@Test
	public void rejectTransactions() throws Exception{
		when(calculationServices.getQueue()).thenReturn(new PriorityBlockingQueue<Transaction>());
		mockMvc.perform(post("/transactions") 
                .content(mapper.writeValueAsString(new Transaction(12.3, System.currentTimeMillis() - windowframe)))
                .contentType(contentType))
                .andExpect(status().isNoContent());
	}
	
	@Test
    public void getStatistic() throws Exception {
        this.mockMvc.perform(get("/statistics"))
                               //.andDo(print())
                               .andExpect(status().isOk());
                //.andExpect(content().string(containsString("Hello World")));
    }
	
}
