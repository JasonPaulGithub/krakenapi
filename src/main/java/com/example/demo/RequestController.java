package com.example.demo;

import com.example.demo.services.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/request/{request}")
    @ResponseBody
    public String request(@PathVariable String request) {
        return requestService.greet(request);
    }

}