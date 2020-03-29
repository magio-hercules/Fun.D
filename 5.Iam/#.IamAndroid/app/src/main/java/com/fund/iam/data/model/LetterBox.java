package com.fund.iam.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LetterBox {

    private String name;
    private String imageUrl;
    private String job;
    private String email;

}