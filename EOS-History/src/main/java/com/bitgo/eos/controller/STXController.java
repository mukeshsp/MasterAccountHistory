package com.bitgo.eos.controller;

import com.bitgo.eos.response.*;
import com.bitgo.eos.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class STXController {

    @Autowired
    StxService stxService;

    @RequestMapping(value = "stx/missingTx", method = RequestMethod.GET, produces = "application/json")
    public HashSet getAccountDetail() {
        return stxService.getMissingTransaction();
    }

}
