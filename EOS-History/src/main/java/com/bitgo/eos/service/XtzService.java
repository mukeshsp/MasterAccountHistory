package com.bitgo.eos.service;

import com.bitgo.eos.response.*;
import com.jayway.jsonpath.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.sql.*;

@Service
public class XtzService {

    @Value("https://api.tzkt.io/v1/accounts")
    public String xtzAccount;

    @Autowired
    RestService restService;

    public XtzResponse getAccountBalance() {

        XtzResponse response = new XtzResponse();
        double balance = 0.0;
        try {
            File file = new File("src/main/resources/account.txt");

            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String st;
            long i = 0;
            while ((st = br.readLine()) != null) {
                try {
                    st = st.trim();
                    balance += getCurrentBalance(st.substring(0, 36), i);
                    i++;
//                    System.out.println(st.substring(0, 38) + " " + balance);
//                    Thread.sleep(10);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
            // Print the string
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        response.setBalance(balance);
        return response;
    }

    private double getCurrentBalance(String accountName, long i) {
        try {
            System.out.print("processing index" + " " + i + "\n");
            String url = xtzAccount + "/" + accountName + "/balance";
//            System.out.print("url"+" "+ url+"\n");
            ResponseEntity<String> result = restService.getRequest(url);
            String responseBody = result.getBody();
            double currentBalance = Double.parseDouble(responseBody.toString());
            ;
            if (currentBalance > 0.0) {
                System.out.print("Index" + i + " account " + accountName + " balance " + currentBalance + "\n");
            }
            return currentBalance;
        } catch (Exception e) {
            System.out.print(e.getMessage() + "\n");
        }
        return 0.0;
    }

}
