package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatResponse (
    @SerializedName("messages") var messages: MutableList<Message>?=null,
) : Parcelable

@Parcelize
data class Message (
    @SerializedName("message") var message: String? = null,
    @SerializedName("direction") var direction: String? = null

) : Parcelable