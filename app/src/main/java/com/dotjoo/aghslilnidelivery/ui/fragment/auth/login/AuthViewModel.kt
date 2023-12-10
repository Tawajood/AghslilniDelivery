package com.dotjoo.aghslilnidelivery.ui.fragment.auth.login


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo. aghslilnidelivery.R
import com.dotjoo. aghslilnidelivery.base.BaseViewModel
import com.dotjoo. aghslilnidelivery.data.param.CheckOtpWithPhoneParam
import com.dotjoo. aghslilnidelivery.data.param.CheckPhoneParam
 import com.dotjoo.aghslilnidelivery.data.param.LoginParams

import com.dotjoo. aghslilnidelivery.data.param.RegisterParams
import com.dotjoo. aghslilnidelivery.data.param.ResetPasswordParams
import com.dotjoo.aghslilnidelivery.data.response.LoginResponse
import com.dotjoo. aghslilnidelivery.util.NetworkConnectivity
import com.dotjoo. aghslilnidelivery.util.Resource
import com.dotjoo. aghslilnidelivery.domain.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(app: Application, val authUserCase: AuthUseCase) :
    BaseViewModel<AuthAction>(app) {
    var email: String? = null
    var otp: String? = null
   // var registerParam: RegisterParams? = null
    var emailVerified =""

fun isValidParamsChangePass(countryCode: String,    newpass: String, confirmpass: String) {
    if (newpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
        false

    } else if (confirmpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
        false

    }/* else if (confirmpass.length<8 ||newpass.length<8 ) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.passmust_be_at_least_8_characters)))
        false

    }*/ else if (!confirmpass.equals(newpass)) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
        false

    } else
        email?.let { resetPass(countryCode,it, otp.toString(), confirmpass) }

}

    fun isVaildLogin(
        phone: String?, password: String?
    ) { // 1 FOR NORMAL  // 0 FOR LOGIN WITH BIOMETRIC
        if (phone.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_phone_number)))
            false
        } else if (password.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false
        } else {


            loginWithPhoneNumber(phone, password)

            true

        }
    }

    fun loginWithPhoneNumber(email: String, password: String, ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, LoginParams(
                        "+20",
                        email, password
                    ) // static 0 for android devices
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(
                                AuthAction.LoginSuccess(
                                    res.data?.data as LoginResponse
                                )
                            )


                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isVaildRegisteration(
        name: String?, country_code: String?, phone: String?,    file_car_lisence: File? ,file_driver_lis: File? ,file_id: File? , pass: String?, repeated_pass: String?
    ) {
        if (name.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_name_msg)))
            false
        } else if (country_code.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_msg_cc)))
            false
        } else if (phone.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_phone_number)))
            false
        } /*else if (lat.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_lat)))
            false
        } else if (lon.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_lat)))
            false
        } else if (address.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_address)))
            false
        }*/ else if (pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false
        } else if (repeated_pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false
        } else if (!repeated_pass.equals(pass)) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false
        } else {
            //  if(emailVerified == email ) {
            register(
                RegisterParams(
                    name,
                    country_code, phone,    file_car_lisence,file_driver_lis,file_id,
                    pass, repeated_pass
                )
            )
        }
        //    else {
        //       register(email)
        //    }


        true

        //  }
    }


    fun register(params: RegisterParams) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data?.data as LoginResponse)?.let {
                                AuthAction.RegisterSucess(
                                    it
                                )
                            }?.let { produce(it) }
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


    fun checkPhone(country_code: String, phone: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, CheckPhoneParam(country_code, phone)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.PhoneChecked(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun checkOtp(countryCode: String, phone: String, otp: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                authUserCase.invoke(
                    viewModelScope, CheckOtpWithPhoneParam(countryCode, phone, otp)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.OtpChecked(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun resetPass(countryCode: String, phone: String, otp: String, pass: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, ResetPasswordParams(countryCode, phone, pass, otp)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.ResetPasswordSucess(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


}
