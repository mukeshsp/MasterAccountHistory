package com.bitgo.eos.service;

import com.bitgo.eos.response.*;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CosmosService {

    @Value("https://api.tzkt.io/v1/accounts")
    public String trxAccount;

    @Autowired
    RestService restService;

    public CosmosMissingTXResponse getMissingTransaction() {
        Set missingTx = new HashSet();
        CosmosMissingTXResponse response = new CosmosMissingTXResponse();
        try {
            // get all transaction using this curl
//            curl --location 'https://zetachain.blockpi.network/lcd/v1/public/cosmos/tx/v1beta1/txs?events=transfer.recipient%3D%27zeta1ausw24f352n2gn7n75maxttkphyd45dejw5lgy%27' \
//            --header 'Cookie: __cf_bm=Z_jiz0b0hc2yHeZIQTj3j4Sd.la7ZxtwQMb2z6zLTGE-1715755278-1.0.1.1-XI63njDYkVKymHozoIprSosdlLd_KE_7296SgUXyBATyhoKFx20MqdxAijoC7cOTbmbO_bQH5tDC0TCjAYzYYw'
//
            String filePath = "src/main/resources/AllTransactionOfOneAddress.json";

            // Read the content of the file as a string
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the JSON string into a JSONArray
            JSONObject object = new JSONObject(content);
//            System.out.println(object);
//            Set<String> keys = object.keySet();
            JSONArray tx_responses = object.getJSONArray("tx_responses");
            for (int i = 0; i < tx_responses.length(); i++) {
                JSONObject obj = tx_responses.getJSONObject(i);
                String txHash = obj.getString("txhash");
                try {
                    String url = "https://app.bitgo.com/api/v2/zeta/public/tx/" + txHash;
                    ResponseEntity<String> result = restService.getRequest(url);

                    if (result.getStatusCodeValue() != 200) {
                        missingTx.add(txHash);
                        System.out.println(txHash);
                        TimeUnit.SECONDS.sleep(10);
                    }
                } catch (Exception e) {
                    missingTx.add(txHash);
                    System.out.println(txHash);
                }

            }

//            for (String key : tx_responses) {
////                System.out.println(key);
//                JSONObject addressData = object.getJSONObject(key);
//                if(addressData.has("tokens.trx:usdc"))
//                {
//                    JSONObject ob= new JSONObject(addressData.getString("tokens.trx:usdt"));
//
//                    String url = "https://apilist.tronscan.org/api/account?address="+key+"&includeToken=true";
//                    ResponseEntity<String> result = restService.getRequest(url);
//                    String responseBody = result.getBody();
//                     JSONArray tokens=new JSONObject(responseBody).getJSONArray("tokens");
//                     for(int i =0;i<tokens.length();i++)
//                     {
//                         JSONObject data=tokens.getJSONObject(i);
//                         if(data.getString("tokenAbbr").equals("USDT"))
//                         {
//                             BigInteger diff = new BigInteger(ob.getString("tokenSpendableBalance")).subtract(new BigInteger(data.getString("balance")));
//                             if(diff.compareTo(BigInteger.ZERO)>0) {
//                                 System.out.println(key + " " + ob.getString("tokenSpendableBalance") + " " + data.getString("balance"));
//                             }
//                         }
//                     }
//                    TimeUnit.SECONDS.sleep(2);
////                    System.out.println(key+" "+ ob.getString("tokenSpendableBalance"));
//
//                }
//            }
//                    org.json.JSONArray jsonArray = new org.json.JSONArray(content);

            // Print the string
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        response.setMissingTx(missingTx);
        return response;
    }
}
