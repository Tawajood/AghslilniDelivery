package com.dotjoo.aghslilnidelivery.base

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
open class BaseResponse(

    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false,
) : Parcelable

data class DevResponse<T>(
    @SerializedName("data")
    var data:  T? = null,


) : BaseResponse() {
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parcelable> {
        override fun createFromParcel(parcel: Parcel): DevResponse<Parcelable> {
            return DevResponse(parcel)
        }

        override fun newArray(size: Int): Array<Parcelable?> {
            return arrayOfNulls(size)
        }
    }
}

