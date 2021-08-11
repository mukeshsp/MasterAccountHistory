package com.bitgo.eos.controller;

import com.bitgo.eos.response.AccountResponse;
import com.bitgo.eos.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/api/user/{userid}", method = RequestMethod.GET, produces = "application/json")
    public AccountResponse getAccountDetail(@PathVariable String userid) {
        return accountService.getAccountBalance(userid);
    }




}
