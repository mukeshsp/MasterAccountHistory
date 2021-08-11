package com.bitgo.eos.response;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class DragonResponse {

    Double totalNow;
    Double receivedFundTill2020;
    Double sentFundTill2020;
    Double deltaTill2020;
    Double totalTillEndOf2020;
    String asOf;

}
