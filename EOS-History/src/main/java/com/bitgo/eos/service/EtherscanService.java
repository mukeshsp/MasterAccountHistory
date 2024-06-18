package com.bitgo.eos.service;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class EtherscanService {

    @Value("${etherAccount}")
    public String etherScanAccount;

    @Autowired
    RestService restService;

    public String getTxJson(String txId) {

        String url = etherScanAccount + "?module=account&action=txlistinternal&txhash=" + txId + "&apikey=SB9U8PWR9HWQHAEQSNQP67K7ACMIM5YXRG";
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


    public void filterFlushTransfer() {
        {
            try {
                ArrayList flushTx= new ArrayList();
                HashMap wallet=new HashMap();
                String filePath = "src/main/resources/attempedTxInSendQ.json";

                // Read the content of the file as a string
                String content = new String(Files.readAllBytes(Paths.get(filePath)));

                // Parse the JSON string into a JSONArray
                org.json.JSONArray jsonArray = new org.json.JSONArray(content);
//                "txType": "FlushTokens"
                System.out.println("[");
                for (int i =0;i<jsonArray.length();i++)
                {
                    org.json.JSONObject obj= jsonArray.getJSONObject(i);
                    int attempted= obj.getInt("attempts");

                     if(attempted>100 && obj.getString( "txType").equals("FlushTokens"))
                     {
                         String tx= obj.getString("txid");
                         String walletID= obj.getString("wallet");

                       boolean flag= isSuccessFull(tx);
                       if(flag) {
                           ArrayList ctx= (ArrayList) wallet.getOrDefault(walletID,new ArrayList());
                           ctx.add(tx);
                           wallet.put(walletID,ctx);
                           System.out.println("'"+tx+"',");
                           TimeUnit.MILLISECONDS.sleep(202);
                           flushTx.add(tx);
                       }
                     }

                }
                System.out.println("]");
                System.out.println("flush transaction size "+ flushTx.size());
                System.out.println("flush transaction size "+ wallet);

            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private boolean isSuccessFull(String txid) {

        String url = "https://api.etherscan.io/api?module=transaction&action=getstatus&txhash=" + txid + "&apikey=SB9U8PWR9HWQHAEQSNQP67K7ACMIM5YXRG";
        ResponseEntity<String> result = restService.getRequest(url);
        String responseBody = result.getBody();
        String msg = JsonPath.parse(responseBody).read("$.message");
        if(msg.equals("OK"))
            return true;

        return false;
    }
}
