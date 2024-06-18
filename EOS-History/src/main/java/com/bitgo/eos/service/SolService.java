package com.bitgo.eos.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.json.JSONObject;

import java.math.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import java.io.*;
import java.util.*;

@Service
public class SolService {

    @Value("${solAccount}")
    public String solAccount;

    @Autowired
    RestService restService;


    public void getStakedDrift() {
        double balance = 0.0;
        try {
            ArrayList notInDB= new ArrayList();
            String filePath = "src/main/resources/staked-value-from-fullnode.json";
            //
            File file = new File("src/main/resources/staked-value-from-db.txt");
            HashMap stakedInDB= getStakedFromDB(file);
            // Read the content of the file as a string
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON string into a JSONArray
            JSONArray jsonArray = new JSONArray(content);
            BigInteger accountBalance = BigInteger.ZERO;
            BigInteger delegatedStake = BigInteger.ZERO;
            BigInteger activeStake = BigInteger.ZERO;
            BigInteger diffSum = BigInteger.ZERO;
            BigInteger totalActiveSumInDB = BigInteger.ZERO;

            for (int i =0;i<jsonArray.length();i++)
            {
                JSONObject obj= jsonArray.getJSONObject(i);
                if(obj.has("activeStake")) {
                    String address = obj.getString("stakePubkey");
                    if (obj.has("accountBalance")) {
                        accountBalance = accountBalance.add(new BigInteger(obj.get("accountBalance").toString()));
                    } else {
                        System.out.println("Not found accountBalance " + address);
                    }

                    if (obj.has("delegatedStake")) {
                        delegatedStake = delegatedStake.add(new BigInteger(obj.get("delegatedStake").toString()));
                    } else {
                        System.out.println("Not found delegatedStake " + address);
                    }
                    activeStake = activeStake.add(new BigInteger(obj.get("activeStake").toString()));

                    if(!stakedInDB.containsKey(address))
                    {
                        notInDB.add(address);
                    }else
                    {
                        totalActiveSumInDB=totalActiveSumInDB.add(new BigInteger(stakedInDB.get(address).toString()));
                        BigInteger diff= (new BigInteger(obj.get("activeStake").toString())).subtract(new BigInteger(stakedInDB.get(address).toString()));
                        diffSum=diffSum.add(diff);
                        System.out.println("Address : "+ address+" diff" + diff);
                        stakedInDB.remove(address);
                    }
                }

            }

            System.out.println("total accountBalance "+ accountBalance);
            System.out.println("total delegatedStake "+ delegatedStake);
            System.out.println("total activeStake "+ activeStake);
            System.out.println("Not in our db  "+ notInDB);
            System.out.println("Inactive   "+ stakedInDB.entrySet());
            BigInteger inActiveSum= BigInteger.ZERO;
            for (Object key:stakedInDB.entrySet())
            {
               inActiveSum = inActiveSum.add(new BigInteger(((Map.Entry) key).getValue().toString()));
            }
                System.out.println("Inactive  sum "+ inActiveSum);
                System.out.println("diff  sum "+ diffSum);
                System.out.println("total active sum in db "+ totalActiveSumInDB);
//            File file = new File("src/main/resources/staked-value-from-fullnode.json");
//
//            BufferedReader br
//                    = new BufferedReader(new FileReader(file));
//
//            String st;
//            long i = 0;
//            while ((st = br.readLine()) != null) {
//                try {
//                    st = st.trim();
//                    String address= st.split(",")[0];
////                    System.out.println(address);
//                    getCurrentBalance(address, i);
//                    i++;
////                    System.out.println(st.substring(0, 38) + " " + balance);
//                    Thread.sleep(10);
//                } catch (Exception e) {
//                    System.out.print(e.getMessage());
//                }
//            }
            // Print the string
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
//        response.setBalance(balance);
//        return response;
    }

    private HashMap getStakedFromDB(File file) {
        HashMap map=new HashMap();
        try{
            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                try {
                    st = st.trim();
                    String address= st.split(",")[0];
                    String val= st.split(",")[1];
//                    System.out.println(address);
                    map.put(address,val);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
            return map;
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return map;
        }
    }

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
//            ResponseEntity<JSONObject> result = restService.postRequestJSONResponse(url, requestBody);
//            JSONObject responseBody = result.getBody();
//            if(((Map)result.getBody().get("result")).get("value")!=null){
//                System.out.print("Index" + i + " account " + accountName + " balance " + (Map)result.getBody().get("result"));
//            }
        } catch (Exception e) {
            System.out.print(e.getMessage() + "\n");
        }
    }
}
