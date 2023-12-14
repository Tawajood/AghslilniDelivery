package com.dotjoo.aghslilnidelivery.fcm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NotificationItem(
    @SerializedName("notification_type")
    val type: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("crystals")
    val crystals: String,
) : Parcelable


enum class FcmNotificationType(val type: Int) {
    NOTIFICATION_LIST(1), SEND_CRYSTALS(2)
}
