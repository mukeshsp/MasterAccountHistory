package com.bitgo.eos.controller;

import com.bitgo.eos.service.EtherscanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtherscanController {

    @Autowired
    EtherscanService etherscanService;

    @RequestMapping(value = "/api/etherscan/{txId}", method = RequestMethod.GET, produces = "application/json")
    public String getAccountDetail(@PathVariable String txId) {
        return etherscanService.getTxJson(txId);
    }

}
