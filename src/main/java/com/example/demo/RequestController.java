package com.example.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/request")
    public @ResponseBody String request() {
        return requestService.greet();
    }

}