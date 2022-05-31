package com.bitgo.eos.response;

import lombok.*;
import org.springframework.web.bind.annotation.*;

@Data
@ResponseBody
public class XtzResponse {

    double balance;

}
