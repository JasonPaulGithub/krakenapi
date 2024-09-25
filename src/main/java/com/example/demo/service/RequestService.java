package com.example.demo.service;

import com.example.demo.models.Device;
import com.example.demo.models.Outage;
import com.example.demo.models.OutageResponse;
import com.example.demo.models.SiteInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RequestService {

    private static final String CUT_OFF_DATE = "2022-01-01T00:00:00.000Z";
    private KrakenService krakenService;

    public ResponseEntity<?> filterOutages(String requestedSite) {
        return ReturnResponse(filteredOutages(requestedSite), requestedSite);
    }

    private List<OutageResponse> filteredOutages(String requestedSite) {
        List<OutageResponse> filteredOutages = new ArrayList<>();
        SiteInfo siteInfo = krakenService.getSiteInfo(requestedSite);
        List<Outage> outages = krakenService.getOutages();

        for (Device device : siteInfo.getDevices()) {
            for (Outage outage : outages) {
                Instant outageBegin = Instant.parse(outage.getBegin());

                if (device.getId().equals(outage.getId())
                        && outageBegin.isBefore(Instant.parse(CUT_OFF_DATE))) {

                    OutageResponse outageResponse = new OutageResponse();

                    outageResponse.setId(outage.getId());
                    outageResponse.setName(device.getName());
                    outageResponse.setBegin(outage.getBegin());
                    outageResponse.setEnd(outage.getEnd());

                    filteredOutages.add(outageResponse);
                }

            }
        }
        return filteredOutages;
    }

    private ResponseEntity<?> ReturnResponse(List<OutageResponse> filteredOutageList, String requestedSite) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(filteredOutageList);
            System.out.println(json);
            return krakenService.postSiteOutages(json, requestedSite);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Could not map to Json.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
