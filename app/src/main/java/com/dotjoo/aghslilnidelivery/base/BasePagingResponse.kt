package com.dotjoo.aghslilnidelivery.base

import android.os.Parcel
import android.os.Parcelable
import com.dotjoo.aghslilnidelivery.base.BaseResponse

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class BasePagingResponse<T> (

    @SerializedName("data"           ) var data         : Data<T>? = null,

) : BaseResponse(){
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(/* p0 = */ parcel, /* p1 = */ flags)
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
 data class Data<T>(
     @SerializedName("current_page"   ) var currentPage  : Int?             = null,
     @SerializedName("first_page_url" ) var firstPageUrl : String?          = null,
     @SerializedName("from"           ) var from         : Int?             = null,
     @SerializedName("last_page"      ) var lastPage     : Int?             = null,
     @SerializedName("per_page"       ) var perPage      : Int?             = null,
     @SerializedName("to"             ) var to           : Int?             = null,
     @SerializedName("total"          ) var total        : Int?             = null,

    @SerializedName("data"           ) var data         : ArrayList<T>  = arrayListOf(),

    )
 data class ItemsInPaging<T>(

    @SerializedName("data"           ) var data         : ArrayList<T>  = arrayListOf(),

    )
/*

*/
