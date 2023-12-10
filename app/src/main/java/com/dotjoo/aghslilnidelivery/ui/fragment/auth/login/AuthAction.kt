package com.dotjoo.aghslilnidelivery.ui.fragment.auth.login

import com.dotjoo.aghslilnidelivery.base.Action
import com.dotjoo.aghslilnidelivery.data.response.LoginResponse

sealed class AuthAction() : Action {

    data class ShowLoading(val show: Boolean) : AuthAction()
    data class ShowFailureMsg(val message: String?) : AuthAction()
     data  class OtpSuccess(val message: String) : AuthAction()
     data  class RegisterSucess(val data: LoginResponse) : AuthAction()
     data  class PhoneChecked(val message: String) : AuthAction()
     data  class OtpChecked(val message: String) : AuthAction()
     data  class ResetPasswordSucess(val message: String) : AuthAction()
    data  class LoginSuccess(val data: LoginResponse ) : AuthAction()
  /*  data class EmailChecked(val data: OtpCheckEmailResponse) : AuthAction()
    data class EmailCheckedAfterRegister(
        val data: OtpCheckEmailAfterRegisterResponse,
     ) : AuthAction()

    data class OtpChecked(val data : OtpCheckEmailResponse) : AuthAction()*/
}
