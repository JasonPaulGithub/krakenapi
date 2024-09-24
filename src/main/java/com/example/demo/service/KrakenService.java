package com.example.demo.service;

import com.example.demo.models.Outage;
import com.example.demo.models.SiteInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class KrakenService {

    public static final String BASE_URL = "https://api.krakenflex.systems/interview-tests-mock-api/v1/";
    public static final String API_KEY = "EltgJ5G8m44IzwE6UN2Y4B4NjPW77Zk6FJK3lL23";

    public List<Outage> getOutages() {
        TypeReference<List<Outage>> type = new TypeReference<>() {
        };
        return (List<Outage>) checkSuccess(makeGetRequest("outages"), type);
    }

    public SiteInfo getSiteInfo(String request) {
        TypeReference<SiteInfo> type = new TypeReference<>() {
        };
        return (SiteInfo) checkSuccess(makeGetRequest("site-info/" + request), type);
    }

//    public ResponseEntity postResult() {
//        return new ResponseEntity(HttpStatus.OK);
//    }

    private ResponseEntity<String> makePostRequest(String url) {
        return RestClient
                .create()
                .post()
                .uri(BASE_URL + url)
                .header("x-api-key", API_KEY)
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<String> makeGetRequest(String url) {
        return RestClient
                .create()
                .get()
                .uri(BASE_URL + url)
                .header("x-api-key", API_KEY)
                .retrieve()
                .toEntity(String.class);
    }

    private Object checkSuccess(ResponseEntity<String> response, TypeReference type) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return parseResponse(response.getBody(), type);
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new ResponseStatusException(response.getStatusCode(), "Error. Consider retrying request.");
        } else {
            throw new ResponseStatusException(response.getStatusCode(), "Error. Request Failed.");
        }
    }

    private Object parseResponse(String response, TypeReference type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, type);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Parsing Json.");
        }
    }

}
