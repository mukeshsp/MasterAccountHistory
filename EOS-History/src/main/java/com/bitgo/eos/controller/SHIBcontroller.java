package com.bitgo.eos.controller;

import com.bitgo.eos.response.*;
import com.bitgo.eos.service.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class SHIBcontroller {

    @Autowired
    SHIBservice trxService;

    @RequestMapping(value = "trx/api/missingTransactions", method = RequestMethod.GET, produces = "application/json")
    public  Map<String, Object> getAccountDetail() {
        return trxService.getAccountBalance();
    }

}
