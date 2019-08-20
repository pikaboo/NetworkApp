package com.lenabru.networkapp.api.models

import com.google.gson.annotations.SerializedName


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

data class ServerUpdate(
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String
)