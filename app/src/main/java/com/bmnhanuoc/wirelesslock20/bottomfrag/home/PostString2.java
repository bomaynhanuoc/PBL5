package com.bmnhanuoc.wirelesslock20.bottomfrag.home;

import com.bmnhanuoc.wirelesslock20.service.ApiService;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostString2 {
    //done
    private String name;
    private String imageUrl;

    public PostString2(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
