package com.dotjoo.aghslilnidelivery.data

import com.dotjoo.aghslilnidelivery.data.param.ActivationParam
import com.dotjoo.aghslilnidelivery.data.param.ChangPasswordParam
import com.dotjoo.aghslilnidelivery.data.param.CheckOtpWithPhoneParam
import com.dotjoo.aghslilnidelivery.data.param.CheckPhoneParam
import com.dotjoo.aghslilnidelivery.data.param.LoginParams
import com.dotjoo.aghslilnidelivery.data.param.NewOrderParam
import com.dotjoo.aghslilnidelivery.data.param.OrderInfoParam
import com.dotjoo.aghslilnidelivery.data.param.RegisterParams
import com.dotjoo.aghslilnidelivery.data.param.ResetPasswordParams
import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
import com.dotjoo.aghslilnidelivery.data.param.toMap
import com.dotjoo.aghslilnidelivery.data.webService.ApiInterface
import com.dotjoo.aghslilnidelivery.fcm.FcmParam
import com.dotjoo.aghslilnidelivery.util.FileManager.toMultiPart
import retrofit2.http.Body
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: ApiInterface
) {
    suspend fun login(param: LoginParams) = api.login(param.countryCode, param.phone, param.pass)
    suspend fun register(param: RegisterParams) = api.register(
        param.toMap(),
        param.file_driver_lis?.toMultiPart("driveing_licence"),
        param.file_car_lisence?.toMultiPart("car_form"),
        param.file_id?.toMultiPart("national_id"),
        param.file_profile_img?.toMultiPart("img"),
        )

  suspend fun updateFcnToken(param: FcmParam) =
         api.updateFcnToken(param.token)
  suspend fun activeAccount(param: ActivationParam) =
         api.activeAccount(param.active)

    suspend fun changePassword(param: ChangPasswordParam) =
        api.changePassword(param.pass, param.new_pass, param.new_pass_confirm)
 suspend fun changePhone(param: CheckPhoneParam) =
        api.changePhone(param.countryCode, param.phone )

    //  suspend fun logout() = api.logout()
    suspend fun deleteAccount() = api.deleteAccount()

    suspend fun resetpassword(param: ResetPasswordParams) =
        api.changePassAfterForgert(param.countryCode, param.phone, param.otp, param.pass)

    suspend fun checkPhone(param: CheckPhoneParam) = api.checkPhone(param.countryCode, param.phone)
    suspend fun checkOTpWIthPhone(param: CheckOtpWithPhoneParam) =
        api.checkOTpWIthPhone(param.countryCode, param.phone, param.otp)
    suspend fun logout() = api.logout()
    suspend fun getCurrentOrder() = api.getCurrentOrder()
    suspend fun getPrevOrder() = api.getPrevOrder()
    suspend fun getNewOrder(param: NewOrderParam) = api.getNewOrder(param.lat, param.lon)
    suspend fun getOrderInfo(param: OrderInfoParam) = api.getOrderInfo(param.orderID)
    suspend fun acceptOrder(param: OrderInfoParam) = api.acceptOrder(param.orderID)
    suspend fun reciveOrder(param: OrderInfoParam) = api.reciveOrder(param.orderID)
    suspend fun deliverOrder(param: OrderInfoParam) = api.deliverOrder(param.orderID)
    suspend fun rejectOrder(param: OrderInfoParam) = api.rejectOrder(param.orderID)
    suspend fun getProfile() = api.getProfile()
    suspend fun updateProfile(param: UpdateProfileParam) = api.updateProfile(
        param.toMap(),
        param.img?.toMultiPart("img"),
    )
    suspend fun getMessages() = api.getMessages()
    suspend fun sendMessage(message:String) = api.sendMessage(message = message)
    suspend fun getNotifications() = api.getNotifications()
    suspend fun contactUs() = api.contactUs()
    suspend fun aboutUs() = api.aboutUs()
    suspend fun terms() = api.terms()
    suspend fun withdraw(amount:String,bankName:String,accNum:String) = api.withdraw(amount,bankName,accNum)

}