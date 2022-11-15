package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(IPRangesController.class)
public class IPRangesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IPRangesController ipRangesController;

    @Test
    public void findIPRangesShouldReturn302ResponseStatusWithValidRegion() throws Exception {
        this.mockMvc.perform(get("/v1/ipranges?validRegion=EU")).andDo(print())
                .andExpect(status().isFound());
    }

    @Test
    public void findIPRangesShouldReturn400ResponseStatusWithInValidRegion() throws Exception {
        this.mockMvc.perform(get("/v1/ipranges?validRegion=ER")).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findIPRangesShouldReturn400ResponseStatusWithNullRegion() throws Exception {
        this.mockMvc.perform(get("/v1/ipranges?validRegion=")).andDo(print())
                .andExpect(status().isBadRequest());
    }


}
