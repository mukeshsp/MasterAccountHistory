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

@Service
public class TRXService {

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
            String filePath = "src/main/resources/transactionWalletId.txt";

            // Read the content of the file as a string
            String[] content = new String(Files.readAllBytes(Paths.get(filePath))).split(",");
          

            for (String key : content) {
                System.out.println("Key: " + key);
                String command1 = "../../bitgo-admin/bin/bgadmin coin trx";
                String command2 = "../../bitgo-admin/bin/bgadmin w " + key;
                String command3 = "../../bitgo-admin/bin/bgadmin wallet get " + key;
                String command4 = "../../bitgo-admin/bin/bgadmin wallet tx --state confirmed --limit=1000";
                StringBuilder output1 = new StringBuilder();
                StringBuilder output2 = new StringBuilder();
                Process process1 = Runtime.getRuntime().exec(command1);
                process1.waitFor();
                Process process2 = Runtime.getRuntime().exec(command2);
                process2.waitFor();                
                ProcessBuilder processBuilder = new ProcessBuilder(command3.split(" "));
                Process process = processBuilder.start();
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(process.getInputStream()));
               
                String line1;
                while ((line1 = reader1.readLine()) != null) {
                    //System.out.println(line1);
                    output1.append(line1).append("\n");
                    if(line1.trim().contains("Press space to continue, other key to abort")){
                        System.out.println("smmskkskksksjksksk");
                        break;
                    }
                }
                String regex = "Root address:\\s*(.+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(output1);
                if (matcher.find()) {
                    String rootAddress = matcher.group(1);
                    System.out.println("Root Address: " + rootAddress);
                    String url = "https://apilist.tronscanapi.com/api/transaction?sort=-timestamp&count=true&start=0&limit=100000000&address=" + rootAddress;
                    String url1 = "https://apilist.tronscanapi.com/api/internal-transaction?limit=10000&start=0&address=" + rootAddress;
                    String url2 = "https://apilist.tronscanapi.com/api/accountv2?address=" + rootAddress;
                    ResponseEntity<String> result2 = restService.getRequest(url2);
                    String responseBody2 = result2.getBody();
                    JSONObject transactionsCount = new JSONObject(responseBody2);
                    Integer count = transactionsCount.getInt("transactions");
                    System.out.println("Count: " + count);

                    ResponseEntity<String> result = restService.getRequest(url);
                    String responseBody = result.getBody();
                    JSONObject transactions = new JSONObject(responseBody);
                    ResponseEntity<String> result1 = restService.getRequest(url1);
                    String responseBody1 = result1.getBody();
                    JSONObject transactions1 = new JSONObject(responseBody1);
                    ProcessBuilder processBuilder1 = new ProcessBuilder(command4.split(" "));
                    Process process4 = processBuilder1.start();
                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(process4.getInputStream()));
                   
                    String line2;
                    while ((line2 = reader2.readLine()) != null) {
                        output2.append(line2).append("\n");
                        if(line2.trim().contains("Press space to continue, other key to abort")){
                            break;
                        }
                    }
                    String[] lines = output2.toString().split("\n");
                    List<String> txidList = new ArrayList<>();
                    Pattern pattern1 = Pattern.compile("│([a-f0-9]{64})│");
                    for (String line : lines) {
                        Matcher matcher1 = pattern1.matcher(line);
                        if (matcher1.find()) {
                            txidList.add(matcher1.group(1));
                        }
                    }
                    String[] txidArray = txidList.toArray(new String[0]);
                    JSONArray dataArray = transactions.getJSONArray("data");
                    JSONArray dataArray2 = transactions1.getJSONArray("data");
                    List<String> notFoundTxIds = new ArrayList<>();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String token = dataObject.getJSONObject("tokenInfo").getString("tokenName");
                        if(token.equals("trx") && !dataObject.has("trigger_info")){
                        String txID = dataObject.getString("hash");
                        boolean found = false;
                       
                        for (String txid : txidArray) {
                            if (txID.equals(txid)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            notFoundTxIds.add(txID);
                            System.out.println("TXID " + txID + " not found in txidArray." );
                        } 
                    }                  
                    }
                    for (int ii = 0; ii < dataArray2.length(); ii++) {
                        System.out.println("Inside "+ ii);
                         JSONObject dataObject1 = dataArray2.getJSONObject(ii);
                         String token1 = dataObject1.getJSONObject("token_list").getJSONObject("tokenInfo").getString("tokenName");
                         if(token1.equals("trx") && !dataObject1.has("trigger_info")){
                         String txID = dataObject1.getString("hash");
                         boolean found = false;
                             for (String txid : txidArray) {
                                 if (txID.equals(txid)) {
                                     found = true;
                                     break;
                                 }
                             }
                             if (!found) {
                                 notFoundTxIds.add(txID);
                                 System.out.println("TXID " + txID + " not found in txidArray.");
                             } 
                         
                         }
                     }
                     response.put(key, notFoundTxIds);
                    
                    System.out.println("Over for "+ key);

                } else {
                    System.out.println("Root address not found.");
                }
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
