package com.bmnhanuoc.wirelesslock20.service;

import com.bmnhanuoc.wirelesslock20.bottomfrag.database.DataBase;
//import com.bmnhanuoc.wirelesslock20.bottomfrag.database.FetchUserResponse;
import com.bmnhanuoc.wirelesslock20.bottomfrag.user.UserFragment;
import com.bmnhanuoc.wirelesslock20.bottomfrag.home.AddUser;
import com.bmnhanuoc.wirelesslock20.bottomfrag.database.DeleteUser;
//import com.bmnhanuoc.wirelesslock20.bottomfrag.database.ListDB;
import com.bmnhanuoc.wirelesslock20.bottomfrag.home.PostString;
import com.bmnhanuoc.wirelesslock20.bottomfrag.home.PostString2;
import com.bmnhanuoc.wirelesslock20.bottomfrag.user.SigninInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://" + UserFragment.printIP() + ":8000/users/";

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-DD HH-mm-ss")
            .create();


    ApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("get-result")
    Call<PostString2> sendPost(); //done

    @GET("open-door")
    Call<PostString> sendOpenDoorPost(); //done

    @POST("sign-in")
    Call<SigninInfo> logIn(@Body SigninInfo signinInfo); //done

    @GET("get-user")
    Call<List<DataBase>> getList(); //done

    @POST("create")
    Call<AddUser> addUser(@Body AddUser addUser); //done

    @POST("delete")
    Call<DeleteUser> deleteUser(@Body DeleteUser deleteUser); //done

    //todo send notification



}
