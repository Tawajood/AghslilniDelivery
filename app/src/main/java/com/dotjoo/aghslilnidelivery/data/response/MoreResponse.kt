package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AboutResponse (
    @SerializedName("about_us") var about: About?=null,
) : Parcelable

@Parcelize
data class About (
    @SerializedName("body") var body: String?=null,
) : Parcelable

@Parcelize
data class ContactResponse (
    @SerializedName("phone") var phone: String?=null,
    @SerializedName("email") var email: String?=null,
) : Parcelable