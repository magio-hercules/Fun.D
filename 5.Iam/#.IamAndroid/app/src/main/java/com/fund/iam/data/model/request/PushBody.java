package com.fund.iam.data.model.request;

import com.fund.iam.data.model.Letter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PushBody {

    private String to;
    private String priority;
    private Letter data;
}