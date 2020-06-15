package com.fund.iam.data.model;

import com.google.gson.annotations.SerializedName;

import org.jsoup.select.Elements;

import java.util.List;

import lombok.Data;
import pl.droidsonroids.jspoon.annotation.Selector;

@Data
public class VersionPage {

    @Selector(".htlgb")
    public List<String> versions;

}
