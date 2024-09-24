package com.example.demo.controller;

import com.example.demo.models.Device;
import com.example.demo.models.Outage;
import com.example.demo.models.SiteInfo;
import com.example.demo.service.KrakenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
public class RequestController {

    public static final String CUT_OFF_DATE = "2022-01-01T00:00:00.000Z";
    private KrakenService krakenService;

    @PostMapping("/request/{request}")
    @ResponseBody
    public ResponseEntity<?> request(@PathVariable String request) {
        return ReturnResponse(filteredList(request));
    }

    private List<Outage> filteredList(String request) {
        List<Outage> filteredOutageList = new ArrayList<>();
        SiteInfo siteInfo = krakenService.getSiteInfo(request);
        List<Outage> outages = krakenService.getOutages();
        for (Device device : siteInfo.getDevices()) {
            for (Outage outage : outages) {
                Instant outageBegin = Instant.parse(outage.getBegin());
                if (device.getId().equals(outage.getId()) && outageBegin.isBefore(Instant.parse(CUT_OFF_DATE))) {
                    filteredOutageList.add(outage);
                }
            }
        }
        return filteredOutageList;
    }

    private ResponseEntity<?> ReturnResponse(List<Outage> filteredOutageList) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(filteredOutageList);
            return new ResponseEntity<>("The following outages have been posted:\n\n" + json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Could not map to Json.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}