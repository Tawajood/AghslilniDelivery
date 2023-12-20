package com.dotjoo.aghslilnidelivery.ui.fragment.main.profile

import com.dotjoo.aghslilnidelivery.base.Action
 import com.dotjoo.aghslilnidelivery.data.response.ItemsInService
 import com.dotjoo.aghslilnidelivery.data.response.ItemsInServiceResponse
 import com.dotjoo.aghslilnidelivery.data.response.LoginResponse
import com.dotjoo.aghslilnidelivery.data.response.ProfileResponse
import com.dotjoo.aghslilnidelivery.data.response.ServiceResponse

sealed class AccountAction : Action {

    data class ShowLoading(val show: Boolean) : AccountAction()
    data class ShowFailureMsg(val message: String?) : AccountAction()


  data class  ShowProfile(val data : ProfileResponse): AccountAction ()
  data class  ProfileUpdated(val msg : String): AccountAction ()
  data class  ShowPhoneUpdated(val msg : String): AccountAction ()
    data class ChangePassword(val msg : String ) : AccountAction()
    data class AccountDeleted(val msg : String ) : AccountAction()
}
