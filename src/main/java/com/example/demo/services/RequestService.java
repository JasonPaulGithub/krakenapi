package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class RequestService {
    public String greet(String request) {
        return request;
    }
}
