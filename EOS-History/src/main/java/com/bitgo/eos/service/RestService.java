package com.bitgo.eos.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class RestService {


    public ResponseEntity<String>  postRequest(String url, String body) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            return restTemplate.postForEntity(uri, body, String.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public ResponseEntity<String>  getRequest(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            URI uri = new URI(url);
            return restTemplate.getForEntity(uri,String.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<String> getRequestForLiveNet(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {

        }
        return null;
    }
}
