package com.bitgo.eos.controller;


import com.bitgo.eos.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class SolController {

    @Autowired
    SolService solService;

    @RequestMapping(value = "/api/sol/{fileName}", method = RequestMethod.GET, produces = "application/json")
    public void getAccountDetail(@PathVariable String fileName) {
         solService.getAccountBalance();
    }

}
