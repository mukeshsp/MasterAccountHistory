package com.bitgo.eos.service;


import com.bitgo.eos.response.*;
import com.jayway.jsonpath.*;
import net.minidev.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.util.*;

@Service
public class SolService {

    @Value("${solAccount}")
    public String solAccount;

    @Autowired
    RestService restService;


    public void getAccountBalance() {

//        XtzResponse response = new XtzResponse();
        double balance = 0.0;
        try {
            File file = new File("src/main/resources/solAddress.txt");

            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String st;
            long i = 0;
            while ((st = br.readLine()) != null) {
                try {
                    st = st.trim();
                    String address= st.split(",")[0];
//                    System.out.println(address);
                    getCurrentBalance(address, i);
                    i++;
//                    System.out.println(st.substring(0, 38) + " " + balance);
                    Thread.sleep(10);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
            // Print the string
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
//        response.setBalance(balance);
//        return response;
    }

    private void getCurrentBalance(String accountName, long i) {
        try {
            System.out.print("index" + " " + i + "\n");
            String url = solAccount;
            String requestBody = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"getAccountInfo\",\"params\":[\""+accountName+"\",{\"encoding\":\"jsonParsed\"}]}";

//            System.out.print("url"+" "+ url+"\n");
            ResponseEntity<JSONObject> result = restService.postRequestJSONResponse(url, requestBody);
//            JSONObject responseBody = result.getBody();
            if(((Map)result.getBody().get("result")).get("value")!=null){
                System.out.print("Index" + i + " account " + accountName + " balance " + (Map)result.getBody().get("result"));
            }
        } catch (Exception e) {
            System.out.print(e.getMessage() + "\n");
        }
    }
}
