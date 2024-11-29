package ru.service.hero.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ApiRequest {

    public static ResultActions getOwnHero(MockMvc mockMvc, String token) throws Exception {
        RequestBuilder getOwnRequest = MockMvcRequestBuilders
                .get("/api/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", String.format("Bearer %s", token));
        return mockMvc.perform(getOwnRequest);
    }

    public static ResultActions createCustomHero(MockMvc mockMvc, String requestAsString, String token) throws Exception {
        MockHttpServletRequestBuilder createRequest = MockMvcRequestBuilders
                .post("/api/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestAsString)
                .header("Authorization", "Bearer " + token);
        return mockMvc.perform(createRequest);
    }

}
