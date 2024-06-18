package com.bitgo.eos.controller;

import com.bitgo.eos.response.*;
import com.bitgo.eos.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrxController {

    @Autowired
    TRXService trxService;

    @RequestMapping(value = "trx/api/getBalance", method = RequestMethod.GET, produces = "application/json")
    public XtzResponse getAccountDetail() {
        return trxService.getAccountBalance();
    }

}
