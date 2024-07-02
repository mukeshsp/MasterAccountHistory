package com.bitgo.eos.service;

import com.bitgo.eos.response.*;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SHIBservice {

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
            String filePath = "src/main/resources/test.json";
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/test.json")));
            JSONObject jsonObject = new JSONObject(content);
            System.out.println(jsonObject.get("query_result"));
       
        //response.setNotFoundTxIds(notFoundTxIds);
        return response;


        // response.setBalance(1);
        // return response;
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;
    }

}
}
