package com.bitgo.eos.response;

import lombok.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@Data
@ResponseBody
public class SHIBresponse {

    private List<String> notFoundTxIds;

// Getter and setter methods for notFoundTxIds
public List<String> getNotFoundTxIds() {
    return notFoundTxIds;
}

public void setNotFoundTxIds(List<String> notFoundTxIds) {
    this.notFoundTxIds = notFoundTxIds;
}

}
