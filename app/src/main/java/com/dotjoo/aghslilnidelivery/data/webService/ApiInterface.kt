package com.dotjoo.aghslilnidelivery.data.webService

import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
import com.dotjoo.aghslilnidelivery.data.response.AboutResponse
import com.dotjoo.aghslilnidelivery.data.response.AlOrdersResponse
import com.dotjoo.aghslilnidelivery.data.response.ChatResponse
import com.dotjoo.aghslilnidelivery.data.response.ContactResponse
import com.dotjoo.aghslilnidelivery.data.response.LoginResponse
import com.dotjoo.aghslilnidelivery.data.response.NotifactionResponse
import com.dotjoo.aghslilnidelivery.data.response.OrderInfoResponse
import com.dotjoo.aghslilnidelivery.data.response.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @POST("driver/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("api/update/fcmToken")
    @FormUrlEncoded
    suspend fun updateFcnToken(
        @Field("device_token") device_token: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("driver/check/phone")
    @FormUrlEncoded
    suspend fun checkPhone(
        @Field("country_code") country_code: String, @Field("phone") phone: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("laundry/send/otp")
    @FormUrlEncoded
    suspend fun checkOTpWIthPhone(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("driver/reset/password")
    @FormUrlEncoded
    suspend fun changePassAfterForgert(
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("otp") otp: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("driver/register")
    @Multipart
    @JvmSuppressWildcards
    suspend fun register(
        @PartMap updateMap: Map<String, RequestBody>,
        @Part image: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("profile/change-password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_password") old_password: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("driver/delete/account")
    @FormUrlEncoded
    suspend fun deleteAccount(
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @POST("driver/update/profile")
    @Multipart
    @JvmSuppressWildcards
    suspend fun updateProfile(
        @PartMap updateMap: Map<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("driver/logout")
    @FormUrlEncoded
    suspend fun logout(): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("driver/get/current/order")
    suspend fun getCurrentOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("driver/get/new/order")
    suspend fun getNewOrder(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("driver/prev/order")
    suspend fun getPrevOrder(
    ): NetworkResponse<DevResponse<AlOrdersResponse>, ErrorResponse>

    @GET("driver/get/order")
    suspend fun getOrderInfo(
        @Query("order_id") order_id: String,

        ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @GET("driver/accept/order")
    suspend fun acceptOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @GET("driver/recive/order")
    suspend fun reciveOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @GET("driver/deliver/order")
    suspend fun deliverOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>


    @GET("driver/reject/order")
    suspend fun rejectOrder(
        @Query("order_id") order_id: String,
    ): NetworkResponse<DevResponse<OrderInfoResponse>, ErrorResponse>

    @GET("driver/get/profile")
    suspend fun getProfile(
    ): NetworkResponse<DevResponse<ProfileResponse>, ErrorResponse>

    @GET("general/get/messages/?type=driver")
    suspend fun getMessages(
    ): NetworkResponse<DevResponse<ChatResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("general/send/message")
    suspend fun sendMessage(
        @Field("type") type: String = "driver",
        @Field("message") message:String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>

    @GET("driver/get/notifications")
    suspend fun getNotifications(
    ): NetworkResponse<DevResponse<NotifactionResponse>, ErrorResponse>
    @GET("general/contact")
    suspend fun contactUs(
    ): NetworkResponse<DevResponse<ContactResponse>, ErrorResponse>
    @GET("general/aboutus")
    suspend fun aboutUs(
    ): NetworkResponse<DevResponse<AboutResponse>, ErrorResponse>
    @GET("general/aboutus")
    suspend fun terms(
    ): NetworkResponse<DevResponse<AboutResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("driver/withdraw/request")
    suspend fun withdraw(
        @Field("amount") type: String,
        @Field("bank_name") bank_name:String,
        @Field("acount_number") acount_number:String,
    ): NetworkResponse<DevResponse<Any>, ErrorResponse>
}