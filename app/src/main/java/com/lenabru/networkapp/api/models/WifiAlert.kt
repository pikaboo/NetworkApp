package com.lenabru.networkapp.api.models

import com.google.gson.annotations.SerializedName


/* 
 * Created by Lena Brusilovski on 2019-08-19
 */

data class WifiAlert(
    @SerializedName("wifiNetworkName") val networkName: String,
    @SerializedName("connectedOn") val connectionDate: String
)