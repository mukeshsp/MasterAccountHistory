package com.bitgo.eos.response;


import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class AccountResponse {

    Double availableFund;
    Double receivedFund;
    Double sendFund;
    Double cpuStaked;
    Double netStaked;
    Double totalTillNow;
    Double totalTillEndOf2020;

    public AccountResponse() {
     this.availableFund=0.0;
     this.receivedFund=0.0;
     this.sendFund=0.0;
     this.cpuStaked=0.0;
     this.netStaked=0.0;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "receivedFund=" + receivedFund +
                ", sendFund=" + sendFund +
                ", availableFund=" + availableFund +
                '}';
    }
}
