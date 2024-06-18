package com.bitgo.eos.service;

import com.bitgo.eos.response.*;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.math.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class TRXService {

    @Value("https://api.tzkt.io/v1/accounts")
    public String trxAccount;

    @Autowired
    RestService restService;

    public XtzResponse getAccountBalance() {

        XtzResponse response = new XtzResponse();
        double balance = 0.0;
        try {
                    ArrayList flushTx= new ArrayList();
                    HashMap wallet=new HashMap();
                    String filePath = "src/main/resources/trxAddresses.json";

                    // Read the content of the file as a string
                    String content = new String(Files.readAllBytes(Paths.get(filePath)));

                    // Parse the JSON string into a JSONArray
            JSONObject object =new JSONObject(content);
//            System.out.println(object);
            Set<String> keys = object.keySet();
            for (String key : keys) {
//                System.out.println(key);
                JSONObject addressData = object.getJSONObject(key);
                if(addressData.has("tokens.trx:usdc"))
                {
                    JSONObject ob= new JSONObject(addressData.getString("tokens.trx:usdt"));

                    String url = "https://apilist.tronscan.org/api/account?address="+key+"&includeToken=true";
                    ResponseEntity<String> result = restService.getRequest(url);
                    String responseBody = result.getBody();
                     JSONArray tokens=new JSONObject(responseBody).getJSONArray("tokens");
                     for(int i =0;i<tokens.length();i++)
                     {
                         JSONObject data=tokens.getJSONObject(i);
                         if(data.getString("tokenAbbr").equals("USDT"))
                         {
                             BigInteger diff = new BigInteger(ob.getString("tokenSpendableBalance")).subtract(new BigInteger(data.getString("balance")));
                             if(diff.compareTo(BigInteger.ZERO)>0) {
                                 System.out.println(key + " " + ob.getString("tokenSpendableBalance") + " " + data.getString("balance"));
                             }
                         }
                     }
                    TimeUnit.SECONDS.sleep(2);
//                    System.out.println(key+" "+ ob.getString("tokenSpendableBalance"));

                }
            }
//                    org.json.JSONArray jsonArray = new org.json.JSONArray(content);

            // Print the string
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        response.setBalance(balance);
        return response;
    }

//    private double getCurrentBalance(String accountName, long i) {
//        try {
//            System.out.print("processing index" + " " + i + "\n");
//            String url = xtzAccount + "/" + accountName + "/balance";
////            System.out.print("url"+" "+ url+"\n");
//            ResponseEntity<String> result = restService.getRequest(url);
//            String responseBody = result.getBody();
//            double currentBalance = Double.parseDouble(responseBody.toString());
//            ;
//            if (currentBalance > 0.0) {
//                System.out.print("Index" + i + " account " + accountName + " balance " + currentBalance + "\n");
//            }
//            return currentBalance;
//        } catch (Exception e) {
//            System.out.print(e.getMessage() + "\n");
//        }
//        return 0.0;
//    }

}
