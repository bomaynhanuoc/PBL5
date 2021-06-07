package com.bmnhanuoc.wirelesslock20.bottomfrag.home;

import com.google.gson.annotations.SerializedName;

public class PostString {

    public PostString(String string) {
        this.string = string;
    }

    private String string;

    @Override
    public String toString() {
        return "PostString{" +
                "string='" + string + '\'' +
                '}';
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }




}

