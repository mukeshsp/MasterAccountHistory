package com.bitgo.eos.service;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EtherscanService {

    @Value("${etherAccount}")
    public String etherScanAccount;

    @Autowired
    RestService restService;

    public String getTxJson(String txId) {

        String url = etherScanAccount + "?module=account&action=txlistinternal&txhash=" + txId + "&apikey=4MJYBTWHX48426E4G3A5I345IPXBDISEER";
        ResponseEntity<String> result = restService.getRequest(url);
        String responseBody = result.getBody();
        return getParsedResponse(responseBody);
    }

    private String getParsedResponse(String responseBody) {
        JSONArray allTransaction = JsonPath.parse(responseBody).read("$.result");
        JSONArray response = new JSONArray();

        for (int i = 0; i < allTransaction.size(); i++) {
            HashMap map = (HashMap) allTransaction.get(i);
            //
            JSONObject fromObject = new JSONObject();
            fromObject.put("address", map.get("from").toString());
            fromObject.put("value", "-" + map.get("value").toString());

            JSONObject toObject = new JSONObject();
            toObject.put("address", map.get("to").toString());
            toObject.put("value", map.get("value").toString());
            response.add(fromObject);
            response.add(toObject);
        }
        return response.toJSONString();
    }


}
