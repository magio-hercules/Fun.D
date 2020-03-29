package com.fund.iam.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LetterBox {

    private String id;
    private String picture;
    private String job;
    private String email;

}