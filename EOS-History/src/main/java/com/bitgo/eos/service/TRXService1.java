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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TRXService1 {

    @Value("https://api.tzkt.io/v1/accounts")
    public String trxAccount;

    @Autowired
    RestService restService;

    public Map<String, Object> getAccountBalance() {
        double balance = 0.0;
   
        Map<String, Object> response = new HashMap<>();
        
        try {
            ArrayList flushTx = new ArrayList();
            HashMap wallet = new HashMap();
            String filePath = "src/main/resources/transactions1.txt";
            String output1 = new String(Files.readAllBytes(Paths.get(filePath)));
            String outputFilePath = "src/main/resources/txid.txt";
            StringBuilder content = new StringBuilder();
            Pattern pattern1 = Pattern.compile("│([a-f0-9]{64})│");
           // BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            try {
                String line;
                // while ((line = reader.readLine()) != null) {
                //     content.append(line);
                //     Matcher matcher1 = pattern1.matcher(line);
                //     if (matcher1.find()) {
                //         writer.write(matcher1.group(1));
                //         writer.newLine();
                //     }
                // }

                String url1 = "https://apilist.tronscanapi.com/api/transaction?sort=-timestamp&count=true&start=0&limit=100000000&address=TXhtzZogaZhA5utAF7AQiVf5BtWR6vQVhu";
                ResponseEntity<String> result1 = restService.getRequest(url1);
                    String responseBody1 = result1.getBody();
                    JSONObject transactions1 = new JSONObject(responseBody1);
                JSONArray dataArray2 = transactions1.getJSONArray("data");
                List<String> txidList = new ArrayList<>();
                for (int ii = 0; ii < dataArray2.length(); ii++) {
                    System.out.println("Inside "+ ii);
                     JSONObject dataObject1 = dataArray2.getJSONObject(ii);
                     String token = dataObject1.getJSONObject("tokenInfo").getString("tokenName");
                     if(token.equals("trx") && !dataObject1.has("trigger_info")){
                     String txID = dataObject1.getString("hash");
                     txidList.add(txID);
                     }
                 }

                  Set<String> fileContents = Files.lines(Paths.get(outputFilePath))
                    .collect(Collectors.toSet());

            // Check each string in the list
                for (String str : txidList) {
                    boolean isPresent = fileContents.contains(str);
                    if(!isPresent)
                    System.out.printf(str);
                }
                 
                System.out.println("content");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        //response.setNotFoundTxIds(notFoundTxIds);
        return response;


        // response.setBalance(1);
        // return response;
    }

}
