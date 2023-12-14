package com.dotjoo.aghslilnidelivery.fcm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FcmParam(
     val token: String,

) : Parcelable

