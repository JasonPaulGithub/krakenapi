package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class RequestService {
    public String greet(String request) {
        return request;
    }
}
