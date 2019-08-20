package com.lenabru.networkapp.api

import com.lenabru.networkapp.api.Api.host
import com.lenabru.networkapp.api.models.ServerResponse
import com.lenabru.networkapp.api.models.ServerUpdate
import com.lenabru.networkapp.api.models.WifiAlert
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


/*
 * Created by Lena Brusilovski on 2019-08-19
 */

interface ApiService {

    @POST("api/users")
    fun sendServerUpdate(@Body body: ServerUpdate): Call<ServerResponse>

    @POST("api/users")
    fun sendWifiAlert(@Body body: WifiAlert): Call<ServerResponse>
}