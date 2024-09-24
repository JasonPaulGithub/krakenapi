package com.example.demo.controller;

import com.example.demo.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RequestController {

    private RequestService requestService;

    @PostMapping("/request/{requestedSite}")
    @ResponseBody
    public ResponseEntity<?> request(@PathVariable String requestedSite) {
        return requestService.filterOutages(requestedSite);
    }

}