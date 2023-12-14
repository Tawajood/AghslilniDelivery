package com.dotjoo.aghslilnidelivery.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderInfoResponse(

    @SerializedName("order"             ) var order           : OrderInfo? = OrderInfo(),
    @SerializedName("total_items_price" ) var totalItemsPrice : String?   = null,

    @SerializedName("recive_from"        ) var recive_from       : String?           = null,
    @SerializedName("deliver_to"        ) var deliver_to       : String?           = null,

) : Parcelable

@Parcelize
data class AlOrdersResponse(
    @SerializedName("orders") var orders: kotlin.collections.ArrayList<Order>  = arrayListOf(),

    ) : Parcelable


@Parcelize
data class Order(
    @SerializedName("id") var id: String? = null,
     @SerializedName("laundry") var laundry: String? = null,
     @SerializedName("items_count") var items_count: String? = null,
     @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("argent") var argent: Int? = null,

    @SerializedName("logo") var logo: String? = null,

    ) : Parcelable

@Parcelize
data class ServiceResponse(

    @SerializedName("service"         ) var services       : ArrayList<ServiceInLaundry> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ItemsInServiceResponse(

    @SerializedName("items"         ) var items       : ArrayList<ItemsInService> = arrayListOf(),

    ) : Parcelable

@Parcelize
data class ServiceInLaundry(

    @SerializedName("id" ) var itemId : String?    = null,
    @SerializedName("name"    ) var name   : String? = null,
    var choosen :Boolean? =  false

) : Parcelable

@Parcelize
data class ItemsInService(


    @SerializedName("id"           ) var id          : String?    = null,
    @SerializedName("laundry_id"   ) var laundryId   : String?    = null,
    @SerializedName("service_id"   ) var serviceId   : String?    = null,
    @SerializedName("price"        ) var price       : String?    = null,
    @SerializedName("argent_price" ) var argentPrice : String?    = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null,
    @SerializedName("name"         ) var name        : String? = null,
    var count :Int ? =0

) : Parcelable

@Parcelize
data class OrderInfo(
    @SerializedName("id"            ) var id           : String?              = null,
    @SerializedName("argent"        ) var argent       : Int?              = null,
    @SerializedName("tax"           ) var tax          : String?              = null,
    @SerializedName("delivery"      ) var delivery     : String?              = null,
    @SerializedName("progress"      ) var progress     : String?              = null,
    @SerializedName("address"      ) var address     : String?              = null,
    @SerializedName("customer_phone"      ) var phone     : String?              = null,
    @SerializedName("total"         ) var total        : String?              = null,
    @SerializedName("customer_name" ) var customerName : String?           = null,
    @SerializedName("lat"           ) var lat          : String?           = null,
    @SerializedName("lon"           ) var lon          : String?              = null,
    @SerializedName("laundry_id"    ) var laundryId    : String?              = null,
    @SerializedName("driver_id"     ) var driverId     : String?           = null,
    @SerializedName("orderitems"    ) var orderitems   : ArrayList<OrderInfoItem> = arrayListOf(),
    @SerializedName("laundry"       ) var laundry      : Laundry?          = Laundry(),
    @SerializedName("driver"        ) var driver       : Driver?           = null,
) : Parcelable

@Parcelize
data class OrderInfoItem(


@SerializedName("id"         ) var id        : Int?    = null,
@SerializedName("order_id"   ) var orderId   : Int?    = null,
@SerializedName("item_id"    ) var itemId    : Int?    = null,
@SerializedName("count"      ) var count     : Int?    = null,
@SerializedName("price"      ) var price     : Int?    = null,
@SerializedName("created_at" ) var createdAt : String? = null,
@SerializedName("updated_at" ) var updatedAt : String? = null,
@SerializedName("item"       ) var item      : Item?   = Item()

) : Parcelable

@Parcelize
data class Item(
@SerializedName("id"           ) var id          : Int?    = null,
@SerializedName("laundry_id"   ) var laundryId   : Int?    = null,
@SerializedName("service_id"   ) var serviceId   : Int?    = null,
@SerializedName("price"        ) var price       : Int?    = null,
@SerializedName("argent_price" ) var argentPrice : Int?    = null,
@SerializedName("created_at"   ) var createdAt   : String? = null,
@SerializedName("updated_at"   ) var updatedAt   : String? = null,
@SerializedName("name"         ) var name        : String? = null,
    @SerializedName("service_name" ) var service_name : String?    = null,

) : Parcelable

@Parcelize
data class Driver(
@SerializedName("id"           ) var id          : String?    = null,
 @SerializedName("country_code"   ) var country_code   : String?    = null,
@SerializedName("phone"        ) var phone       : String?    = null,
@SerializedName("driveing_licence" ) var driveing_licence : String?    = null,
@SerializedName("car_form"   ) var car_form   : String? = null,
@SerializedName("national_id"   ) var national_id   : String? = null,
@SerializedName("name"         ) var name        : String? = null,
@SerializedName("img"         ) var img        : String? = null,
@SerializedName("updated_at"         ) var updated_at        : String? = null

) : Parcelable
