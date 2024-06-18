package com.bitgo.eos.controller;

import com.bitgo.eos.response.*;
import com.bitgo.eos.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class CosmosController {

    @Autowired
    CosmosService cosmosServicervice;

    @RequestMapping(value = "cosmos/missingTx", method = RequestMethod.GET, produces = "application/json")
    public CosmosMissingTXResponse getAccountDetail() {
        return cosmosServicervice.getMissingTransaction();
    }

}
