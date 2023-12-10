package com.dotjoo.aghslilnidelivery.data.param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class OrderInfoParam(val orderID: String = "",val type: Int? = null )
data class NewOrderParam(val lat: String = "", val lon: String = "", )
