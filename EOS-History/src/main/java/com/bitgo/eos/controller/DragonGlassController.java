package com.bitgo.eos.controller;

import com.bitgo.eos.response.AccountResponse;
import com.bitgo.eos.response.DragonResponse;
import com.bitgo.eos.service.AccountService;
import com.bitgo.eos.service.DragonGlassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DragonGlassController {

    @Autowired
    DragonGlassService dragonGlassService;

    @RequestMapping(value = "/api/dragon/user/{accountName}", method = RequestMethod.GET, produces = "application/json")
    public DragonResponse getAccountDetail(@PathVariable String accountName) {
        return dragonGlassService.getAccountBalance(accountName);
    }

}
