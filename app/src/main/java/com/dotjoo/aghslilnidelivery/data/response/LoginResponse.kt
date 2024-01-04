package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
@Parcelize
data class LoginResponse(  @SerializedName("token"   ) var token   : String?  = null,
                           @SerializedName("laundry" ) var laundry : Laundry? = Laundry()
) : Parcelable


@Parcelize
data class Laundry(
    @SerializedName("name"         ) var name        : String? = null,
    @SerializedName("phone"        ) var phone       : String? = null,
    @SerializedName("rate"        ) var rate       : String? = null,
    @SerializedName("country_code" ) var countryCode : String? = null,
    @SerializedName("logo"         ) var logo        : String? = null,
    @SerializedName("address"      ) var address     : String? = null,
    @SerializedName("id"      ) var id     : String? = null,
    @SerializedName("lat"          ) var lat         : String? = null,
    @SerializedName("lon"          ) var lon         : String? = null,
    @SerializedName("activation"          ) var activation         : Boolean? = null,
 ) : Parcelable
