package com.leesc.tazza.data.model;

import com.google.gson.Gson;

public class JsonBody {

    private int no;

    private boolean result;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
