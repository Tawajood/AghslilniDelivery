package com.dotjoo.aghslilnidelivery.data.param

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File



data class UpdateProfileParam(
    val name: String,
    val country_code: String,
    val phone: String,
    val  img: File?,
)



fun UpdateProfileParam.toMap(): Map<String, RequestBody>{
    val itemMap = hashMapOf<String, RequestBody>()
    itemMap["name"] = name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["country_code"] = country_code.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["phone"] = phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    return itemMap
}