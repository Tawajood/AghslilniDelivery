package com.dotjoo.aghslilnidelivery.data.param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class LoginParams(val countryCode: String = "", val phone: String = "", val pass: String = "")

data class RegisterParams(

    val name: String,
    val country_code: String,
    val phone: String,
    val  file_car_lisence: File?,
    val  file_driver_lis: File?,
    val file_id: File?,
    val password: String,
    val password_confirmation: String?
)



fun RegisterParams.toMap(): Map<String, RequestBody>{

    val itemMap = hashMapOf<String, RequestBody>()

    itemMap["name"] = name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

    itemMap["password"] = password.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["password_confirmation"] = password_confirmation.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["country_code"] = country_code.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["phone"] = phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


    return itemMap
}
data class ResetPasswordParams(
    val countryCode: String = "",
    val phone: String = "",
    val pass: String = "",
    val otp: String = ""
)

data class ChangPasswordParam(
    val pass: String = "", val new_pass: String = "", val new_pass_confirm: String = ""
)

data class CheckPhoneParam(val countryCode: String = "", val phone: String = "")
data class CheckOtpWithPhoneParam(
    val countryCode: String = "", val phone: String = "", val otp: String = ""
)

@Parcelize
data class AddAddressParams(
    var lat: String?,
    var long: String?,
    var city: String?,
    var district: String?,
    var street: String?,
    var building: String?,
    var floor: String?,
    var flat: String?,
    var fullAddress: String?
) : Parcelable
