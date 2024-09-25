package com.example.demo;

import com.example.demo.controller.RequestController;
import com.example.demo.models.Device;
import com.example.demo.models.Outage;
import com.example.demo.models.SiteInfo;
import com.example.demo.service.KrakenService;
import com.example.demo.service.RequestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(RequestController.class)
class WebMockTest {

    @MockBean
    private RequestService requestService;

    @MockBean
    private KrakenService krakenService;

    @Test
    void requestControllerServiceShouldReturnOkResponse() {

        List<Outage> outages = new ArrayList<>();
        Outage outage = new Outage();
        outage.setBegin("2021-01-01T00:00:00.000Z");
        outage.setEnd("2022-01-01T00:00:00.000Z");
        outage.setId(UUID.randomUUID().toString());
        outages.add(outage);

        ResponseEntity<String> ok = new ResponseEntity<>(HttpStatus.OK);

        ArrayList<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setId(UUID.randomUUID().toString());
        device.setName("Battery 1");
        devices.add(device);

        SiteInfo siteInfo = new SiteInfo();
        siteInfo.setId("norwich-pear-tree");
        siteInfo.setDevices(devices);
        siteInfo.setName("Norwich Pear Tree");

        when(krakenService.getOutages()).thenReturn(outages);
        when(krakenService.postSiteOutages("[{\"id\": \"0133935a-6b33-4a73-b9a5-bee1636d4f98\",\"begin\": \"2022-07-07T15:29:50.072Z\",\"end\": \"2022-09-16T11:51:28.312Z\"}]", "norwich-pear-tree")).thenReturn(ok);
        when(krakenService.getSiteInfo("norwich-pear-tree")).thenReturn(siteInfo);
        when(requestService.filterOutages("norwich-pear-tree")).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertEquals(requestService.filterOutages("norwich-pear-tree").getStatusCode(), HttpStatus.OK);
    }
}
