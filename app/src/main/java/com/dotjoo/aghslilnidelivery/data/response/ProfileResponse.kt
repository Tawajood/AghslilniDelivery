package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileResponse(
    @SerializedName("driver") val driver: Profile,
): Parcelable

@Parcelize
data class Profile(
    @SerializedName("name") val name: String,
    @SerializedName("country_code") val country_code: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("img") val img: String,
    @SerializedName("balance") val balance: Double,
    @SerializedName("total") val total: Double,
): Parcelable

