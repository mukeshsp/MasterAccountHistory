package com.bitgo.eos.service;

import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.concurrent.*;

@Service
public class StxService {

    @Autowired
    RestService restService;

    public HashSet getMissingTransaction() {
        HashSet missingTx = new HashSet();
        try {
            //fetch transaction from onchain using this API
            // curl --location 'https://api.hiro.so/extended/v2/addresses/SM10V5P1EX7AVQAYX2G38R62C3S0N1ETS1XB7X6P3/transactions?limit=50&offset=0'

            String address = "SM10V5P1EX7AVQAYX2G38R62C3S0N1ETS1XB7X6P3"; //ToDo update your address
            int limit = 50;
            int offset = 0;
            boolean completed = false;

            while (!completed) {
                String url = "https://api.hiro.so/extended/v2/addresses/" + address + "/transactions?limit=" + limit + "&offset=" + offset;
                ResponseEntity<String> result = restService.getRequest(url);
                JSONObject responseBody = new JSONObject(result.getBody());
                System.out.println("Fetched transaction Offset: "+ offset);
                //set next offset
                offset += limit;

                JSONArray results = responseBody.getJSONArray("results");
                if (results.isEmpty()) {
                    completed = true;
                    System.out.println("Completed task !!");
                    continue;
                }
                for (int i = 0; i < results.length(); i++) {
                    String txId = results.getJSONObject(i).getJSONObject("tx").getString("tx_id");
                    //check tx is present in bitgo or not
                    try {
                        String bitgoUrl = "https://app.bitgo.com/api/v2/stx/public/tx/" + txId;
                        ResponseEntity<String> result1 = restService.getRequest(bitgoUrl);
                        if (result1.getStatusCodeValue() != 200) {
                            missingTx.add(txId);
                            System.out.println(txId);
                        }
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        missingTx.add(txId);
                        System.out.println("got error " + txId);
                    }
                }
                TimeUnit.SECONDS.sleep(5);

            }
        } catch (Exception e) {

            System.out.println("got error " + e);
        }
        return missingTx;
    }
}

