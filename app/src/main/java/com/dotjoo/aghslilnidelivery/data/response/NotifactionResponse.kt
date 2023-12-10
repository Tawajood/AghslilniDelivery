package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotifactionResponse (
    @SerializedName("notifications") var notificationItem: ArrayList<NotificationItem>? = null
) : Parcelable

@Parcelize
data class NotificationItem (
    @SerializedName("id") var id: String? = null,
    @SerializedName("laundry_id") var laundry_id: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,

    ) : Parcelable