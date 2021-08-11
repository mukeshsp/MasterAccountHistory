package com.bitgo.eos.service;

import com.bitgo.eos.response.AccountResponse;
import com.bitgo.eos.response.LiveNetResponse;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LiveNetService {

    @Value("${liveNetAccount}")
    public String liveAccount;

    @Autowired
    RestService restService;


    public LiveNetResponse getAccountBalance(String accountName) {
        LiveNetResponse response = new LiveNetResponse();
        getCurrentBalance(accountName, response);
        getSentAndReceivedFund(accountName, response);
        calculateTotalTill2020(response);

        return response;
    }

    private void calculateTotalTill2020(LiveNetResponse response) {
        double remaining = response.getReceivedFundAfter2020() - response.getSentFundAfter2020();
        response.setDeltaAfter2020(remaining);
        response.setTotalTillEndOf2020(response.getTotalTillNow() - remaining);
    }

    private void getSentAndReceivedFund(String accountName, LiveNetResponse response) {
        Double receivedFund = 0.0;
        Double sentFund = 0.0;
        long count = 0;
        HashMap<String, Double> map = new HashMap<>();
        try {
            String marker = "";
            boolean flag = true;
            while (flag) {
                String url = liveAccount + "/account_transactions/" + accountName;
                if (!marker.isEmpty()) {
                    url += "?marker=" + marker;
                }
                ResponseEntity<String> result = restService.getRequestForLiveNet(url);
                String responseBody = result.getBody();

                JSONArray allData = JsonPath.parse(responseBody).read("$.transactions[*]");

                if (!allData.isEmpty()) {

                    for (int i = 0; i < allData.size(); i++) {
                        HashMap object = (HashMap) allData.get(i);
                        String date = object.get("date").toString().substring(0, 4);
//                        System.out.println(object.get("date").toString());
                        int year = Integer.parseInt(date);
                        //year condition year > 2020 &&
                        if (year > 2020) {
                            count++;
                            String destination = JsonPath.parse(object).read("$.details.instructions.destination");
                            String currency = JsonPath.parse(object).read("$.details.instructions.amount.currency");
                            if (accountName.equals(destination)) {
                                System.out.println(object.get("date").toString() + "  marker: " + marker);
                                double val = Double.parseDouble(JsonPath.parse(object).read("$.details.instructions.amount.amount").toString());
                                map.put(currency, map.getOrDefault(currency, 0.0) + val);
                                receivedFund += val;
                            } else {
                                double val = Double.parseDouble(JsonPath.parse(object).read("$.details.instructions.amount.amount").toString());
                                map.put(currency, map.getOrDefault(currency, 0.0) - val);
                                sentFund += val;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
                String m = JsonPath.parse(responseBody).read("$.marker");
                if (m.isEmpty()) {
                    flag = false;
                } else {
                    marker = m;
//                    System.out.println("Marker:  " + marker);
//                    TimeUnit.SECONDS.sleep(5);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Total Transaction " + count);
        for (Map.Entry<String, Double> entry : map.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());

        response.setReceivedFundAfter2020(receivedFund);
        response.setSentFundAfter2020(sentFund);

    }

    private void getCurrentBalance(String accountName, LiveNetResponse response) {

        String url = liveAccount + "/account_state/" + accountName;
        ResponseEntity<String> result = restService.getRequestForLiveNet(url);
        String responseBody = result.getBody();
        double currentBalance = JsonPath.parse(responseBody).read("$.balances.XRP");
        response.setTotalTillNow(currentBalance);
        response.setAsOf(LocalDateTime.now().toString());
    }
}
