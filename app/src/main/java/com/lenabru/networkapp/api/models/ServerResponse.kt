package com.lenabru.networkapp.api.models

import com.google.gson.annotations.SerializedName


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

data class ServerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("createdAt") val createdAt: String
)