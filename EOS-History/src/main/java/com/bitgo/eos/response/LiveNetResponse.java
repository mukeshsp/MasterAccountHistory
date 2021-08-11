package com.bitgo.eos.response;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class LiveNetResponse {

    Double receivedFundAfter2020;
    Double sentFundAfter2020;
    Double totalTillNow;
    Double deltaAfter2020;
    Double totalTillEndOf2020;
    String asOf;
}
