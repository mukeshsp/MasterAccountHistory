package com.bitgo.eos.service;

import com.bitgo.eos.response.AccountResponse;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AccountService {

    @Value("${get.account}")
    public String account;

    @Value("${get.history}")
    public String history;

    @Value("${staked}")
    public String staked;

    @Autowired
    RestService restService;

    public AccountResponse getAccountBalance(String userid) {
        try {
            AccountResponse response = new AccountResponse();
            double receivedFund = getReceivedTokenThisYear(userid);
            response.setReceivedFund(receivedFund);

            double sentFund = getSentTokenThisYear(userid);
            response.setSendFund(sentFund);

            getCpuStakedAndNetStacked(userid, response);

            JSONObject object = new JSONObject();
            object.put("account_name", userid);
            TimeUnit.SECONDS.sleep(10);
            ResponseEntity<String> result = restService.postRequest(account, object.toJSONString());
            System.out.println(result.getBody());
            String responseString = result.getBody();
            String available = JsonPath.parse(responseString).read("$.core_liquid_balance");
            response.setAvailableFund(Double.parseDouble(available.split(" ")[0]));
            calculateTotalEosBalance(response);
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void calculateTotalEosBalance(AccountResponse response) {
     response.setTotalTillNow(response.getAvailableFund()+response.getCpuStaked()+response.getNetStaked());
     double remaining=response.getReceivedFund()-response.getSendFund();
     response.setTotalTillEndOf2020(response.getAvailableFund()-remaining);
    }

    private void getCpuStakedAndNetStacked(String accountName, AccountResponse accountResponse) {
       try{
        boolean moreDate = true;
        String lowerBound = "";
        Double cpuStaked = 0.0;
        Double netStaked = 0.0;

        while (moreDate) {
            JSONObject request = new JSONObject();
            request.put("json", true);
            request.put("code", "eosio");
            request.put("scope", accountName);
            request.put("table", "delband");
            request.put("lower_bound", lowerBound);
            request.put("upper_bound", "");
            request.put("index_position", 1);
            request.put("key_type", "i64");
            request.put("limit", -1);
            request.put("reverse", false);
            request.put("show_payer", false);
            ResponseEntity<String> result = restService.postRequest(staked, request.toJSONString());
            System.out.println(result);

            JSONArray cpuDataArray = JsonPath.parse(result.getBody()).read("$.rows[*].cpu_weight");

            if (!cpuDataArray.isEmpty())
                for (int i = 0; i < cpuDataArray.size(); i++) {
                    cpuStaked += Double.parseDouble(cpuDataArray.get(i).toString().split(" ")[0]);
                }
            JSONArray netData = JsonPath.parse(result.getBody()).read("$.rows[*].net_weight");
            if (!netData.isEmpty())
                for (int i = 0; i < cpuDataArray.size(); i++) {
                    netStaked += Double.parseDouble(netData.get(i).toString().split(" ")[0]);
                }
            moreDate = JsonPath.parse(result.getBody()).read("$.more");
            if (moreDate) {
                lowerBound = JsonPath.parse(result.getBody()).read("$.next_key");
                TimeUnit.SECONDS.sleep(10);
            }
        }
        accountResponse.setCpuStaked(cpuStaked);
        accountResponse.setNetStaked(netStaked);
    }catch (Exception e)
       {
           System.out.println("getCpuStakedAndNetStacked error"+e.getMessage());
           return ;
       }
    }

    public Double getReceivedTokenThisYear(String accountName) {
        Double receivedFund = 0.0;
        try {
            int skip = 0, limit = 1000;

            boolean flag = true;
            while (flag) {
                String url = history + "?account=" + accountName + "&filter=eosio.token%3A*&skip=" + skip + "&limit=" + limit + "&sort=desc&after=2020-12-31T23:59:59.000Z&before=2021-12-31T23:59:59.000Z&transfer.to=" + accountName;
                ResponseEntity<String> result = restService.getRequest(url);
                String responseBody = result.getBody();
                int totalTransaction = JsonPath.parse(responseBody).read("$.total.value");
                if (totalTransaction <= skip + limit) {
                    flag = false;
                } else {
                    skip += limit;
                    TimeUnit.SECONDS.sleep(10);
                }
                JSONArray receivedData = JsonPath.parse(responseBody).read("$.actions[*].act.data.amount");
                if (!receivedData.isEmpty())
                    for (int i = 0; i < receivedData.size(); i++) {
                        receivedFund += Double.parseDouble(receivedData.get(i).toString());
                    }
            }

        }catch (Exception e)
        {
        System.out.println("receivedFund error "+e.getMessage());
        }
        return receivedFund;
    }

    public Double getSentTokenThisYear(String accountName) {
        int skip = 0, limit = 1000;
        Double sentFund = 0.0;
        try {
            boolean flag = true;
            while (flag) {
                String url = history + "?account=" + accountName + "&filter=eosio.token%3A*&skip=" + skip + "&limit=" + limit + "&sort=desc&after=2020-12-31T23:59:59.000Z&before=2021-12-31T23:59:59.000Z&transfer.from=" + accountName;
                ResponseEntity<String> result = restService.getRequest(url);
                String responseBody = result.getBody();
                int totalTransaction = JsonPath.parse(responseBody).read("$.total.value");
                if (totalTransaction <= skip + limit) {
                    flag = false;
                } else {
                    skip += limit;
                }
                JSONArray receivedData = JsonPath.parse(responseBody).read("$.actions[*].act.data.amount");
                if (!receivedData.isEmpty())
                    for (int i = 0; i < receivedData.size(); i++) {
                        sentFund += Double.parseDouble(receivedData.get(i).toString());
                    }
            }
        }catch (Exception e)
        {
            System.out.println("sentFund error "+e.getMessage());
        }
        return sentFund;

    }


}
