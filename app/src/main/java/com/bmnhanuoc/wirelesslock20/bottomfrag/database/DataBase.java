package com.bmnhanuoc.wirelesslock20.bottomfrag.database;

import com.google.gson.annotations.SerializedName;

public class DataBase {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("imageUrl")
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
