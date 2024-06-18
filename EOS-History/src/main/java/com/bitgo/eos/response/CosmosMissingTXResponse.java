package com.bitgo.eos.response;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Data
@ResponseBody
public class CosmosMissingTXResponse {

    Set missingTx;

}
