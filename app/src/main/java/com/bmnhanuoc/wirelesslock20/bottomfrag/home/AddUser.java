package com.bmnhanuoc.wirelesslock20.bottomfrag.home;

import com.google.gson.annotations.SerializedName;

public class AddUser {
    @SerializedName("name")
    String name;
	@SerializedName("imageUrl")
    String url;

    public AddUser(String name,String url) {
        this.name = name;
		this.url = url;
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

    @Override
    public String toString() {
        return "AddUser{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
