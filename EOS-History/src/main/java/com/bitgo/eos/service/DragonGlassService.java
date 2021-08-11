package com.bitgo.eos.service;

import com.bitgo.eos.response.AccountResponse;
import com.bitgo.eos.response.DragonResponse;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class DragonGlassService {

    @Value("${dragonAccount}")
    public String dragonAccount;

    @Autowired
    RestService restService;

    public DragonResponse getAccountBalance(String accountName) {
        DragonResponse response = new DragonResponse();
        try {
            getCurrentBalance(accountName, response);
            getSentAndReceivedFund(accountName, response);
            calculateTotalTill2020(response);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return response;
    }

    private void calculateTotalTill2020(DragonResponse response) {
        double remaining=response.getReceivedFundTill2020()-response.getSentFundTill2020();
        response.setDeltaTill2020(remaining);
        response.setTotalTillEndOf2020(remaining);
    }

    private void getSentAndReceivedFund(String accountName, DragonResponse response) {
        Double receivedFund = 0.0;
        Double sentFund = 0.0;
        try {
            int offset = 0, limit = 1000;

            boolean flag = true;
            while (flag) {
                String url = dragonAccount + "/" + accountName + "/transfers?offset=" + offset + "&limit=" + limit + "&sort=consensusTime&order=desc";
                ResponseEntity<String> result = restService.getRequest(url);
                String responseBody = result.getBody();
                int totalTransaction = JsonPath.parse(responseBody).read("$.totalCount");
                if (totalTransaction <= offset + limit) {
                    flag = false;
                } else {
                    offset += limit;
                    TimeUnit.SECONDS.sleep(5);
                }
                JSONArray allData = JsonPath.parse(responseBody).read("$.data[*]");
                if (!allData.isEmpty())
                    for (int i = 0; i < allData.size(); i++) {
                        HashMap object = (HashMap) allData.get(i);
                        String consensusTime = object.get("consensusTime").toString().substring(0, 4);
                        int year = Integer.parseInt(consensusTime);
                        //year condition year > 2020 &&
                        if (year <= 2020 && "SUCCESS".equals(object.get("status").toString())) {
                            if ("Debit".equals(object.get("transactionDirection"))) {
                                sentFund += Double.parseDouble(object.get("amount").toString());
                            } else if ("Credit".equals(object.get("transactionDirection"))) {
                                receivedFund += Double.parseDouble(object.get("amount").toString());
                            }
                        }
                    }
            }

        } catch (Exception e) {

        }
        response.setReceivedFundTill2020(receivedFund/100000000);
        response.setSentFundTill2020(sentFund/100000000);
    }

    private void getCurrentBalance(String accountName, DragonResponse response) {
        String url = dragonAccount + "/" + accountName + "/balance";
        ResponseEntity<String> result = restService.getRequest(url);
        String responseBody = result.getBody();
        double currentBalance = JsonPath.parse(responseBody).read("$.balance");
        String asOf = JsonPath.parse(responseBody).read("$.asOf");
        response.setTotalNow(currentBalance);
        response.setAsOf(asOf);

    }
}
