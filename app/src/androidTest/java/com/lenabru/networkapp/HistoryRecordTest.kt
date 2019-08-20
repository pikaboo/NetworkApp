package com.lenabru.networkapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lenabru.networkapp.database.models.HistoryRecord
import com.lenabru.networkapp.repo.isNetworkSwitch
import com.lenabru.networkapp.repo.isOnSameNetwork
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith


/* 
 * Created by Lena Brusilovski on 2019-08-20
 */

@RunWith(AndroidJUnit4::class)
class HistoryRecordTest {


    @Test
    fun nullRecordIsSameNetwork_ShouldReturnFalse() {
        val thisRecord: HistoryRecord? = null
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)

        assertThat(thisRecord.isOnSameNetwork(otherRecord), `is`(false))
    }

    @Test
    fun nullOtherRecordIsSameNetwork_IsSameNetwork_ShouldReturnFalse() {
        val otherRecord: HistoryRecord? = null
        val thisRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)

        assertThat(thisRecord.isOnSameNetwork(otherRecord), `is`(false))
    }

    @Test
    fun sameThisRecordToNetworkOtherRecordToNetwork_diffFromNetwork_IsSameNetwork_ShouldReturnFalse() {
        val thisRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.NoConnectivity, HistoryRecord.Wifi)

        assertThat(thisRecord.isOnSameNetwork(otherRecord), `is`(false))
    }


    @Test
    fun sameThisRecordFromNetworkOtherRecordFromNetwork_diffToNetwork_IsSameNetwork_ShouldReturnFalse() {
        val thisRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.NoConnectivity, HistoryRecord.Mobile)
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.NoConnectivity, HistoryRecord.Wifi)

        assertThat(thisRecord.isOnSameNetwork(otherRecord), `is`(false))
    }

    @Test
    fun sameThisRecordToNetworkOtherRecordFromNetwork_equalToNetwork_IsSameNetwork_ShouldReturnTrue() {
        val thisRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.NoConnectivity, HistoryRecord.Mobile)
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Mobile, HistoryRecord.Mobile)

        assertThat(thisRecord.isOnSameNetwork(otherRecord), `is`(true))
    }

    @Test
    fun nullThisRecord_OtherRecord_IsNetworkSwitch_ShouldReturnTrue(){
        val thisRecord: HistoryRecord? = null
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)

        assertThat(thisRecord.isNetworkSwitch(otherRecord), `is`(true))
    }


    @Test
    fun thisRecord_NullOtherRecord_IsNetworkSwitch_ShouldReturnTrue(){
        val otherRecord: HistoryRecord? = null
        val thisRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)

        assertThat(thisRecord.isNetworkSwitch(otherRecord), `is`(true))
    }

    @Test
    fun samethisRecordToNetwork_OtherRecordToNetwork_IsNetworkSwitch_ShouldReturnFalse(){
        val thisRecord = HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)

        assertThat(thisRecord.isNetworkSwitch(otherRecord), `is`(false))
    }

    @Test
    fun differntthisRecordToNetwork_OtherRecordToNetwork_IsNetworkSwitch_ShouldReturnTrue(){
        val thisRecord = HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.Wifi)
        val otherRecord =
            HistoryRecord(System.currentTimeMillis(), HistoryRecord.Wifi, HistoryRecord.NoConnectivity)

        assertThat(thisRecord.isNetworkSwitch(otherRecord), `is`(true))
    }
}