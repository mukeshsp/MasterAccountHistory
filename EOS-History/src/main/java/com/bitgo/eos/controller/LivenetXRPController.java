package com.bitgo.eos.controller;

import com.bitgo.eos.response.AccountResponse;
import com.bitgo.eos.response.DragonResponse;
import com.bitgo.eos.response.LiveNetResponse;
import com.bitgo.eos.service.DragonGlassService;
import com.bitgo.eos.service.LiveNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivenetXRPController {

    @Autowired
    LiveNetService liveNetService;

    @RequestMapping(value = "/api/livenet/user/{accountName}", method = RequestMethod.GET, produces = "application/json")
    public LiveNetResponse getAccountDetail(@PathVariable String accountName) {
        return liveNetService.getAccountBalance(accountName);
    }

}
