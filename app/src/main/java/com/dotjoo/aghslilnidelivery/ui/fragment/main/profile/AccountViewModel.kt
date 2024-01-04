package com.dotjoo.aghslilnidelivery.ui.fragment.main.profile

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dotjoo.aghslilnidelivery.R
 import com.dotjoo.aghslilnidelivery.data.PrefsHelper
 import com.dotjoo.aghslilnidelivery.data.param.ChangPasswordParam
 import com.dotjoo.aghslilnidelivery.data.response.ItemsInService
 import com.dotjoo.aghslilnidelivery.data.response.LoginResponse
import com.dotjoo.aghslilnidelivery.data.response.ServiceInLaundry
  import com.dotjoo.aghslilnidelivery.util.NetworkConnectivity
import com.dotjoo.aghslilnidelivery.util.Resource
import com.dotjoo.aghslilnidelivery.base.BaseViewModel
import com.dotjoo.aghslilnidelivery.data.param.CheckPhoneParam
import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
import com.dotjoo.aghslilnidelivery.data.response.ProfileResponse
import com.dotjoo.aghslilnidelivery.domain.AccountUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountViewModel
@Inject constructor(
    app: Application,
    val useCase: AccountUseCase
) : BaseViewModel<AccountAction>(app) {


   fun getProfileData() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,1
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AccountAction.ShowProfile(res.data?.data as ProfileResponse))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

       fun updateProfile(params: UpdateProfileParam) {
           if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
               produce(AccountAction.ShowLoading(true))

               viewModelScope.launch {
                   var res = useCase.invoke(
                       viewModelScope, params
                   ) { res ->
                       when (res) {
                           is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                           is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                           is Resource.Success -> {
                             res.data?.message?.let {   produce(AccountAction.ProfileUpdated(it)) }

                           }
                       }
                   }
               }
           } else {
               produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
           }
       }


    fun isValidParamsChangePass(oldpass: String, newpass: String, confirmpass: String) {
        if (oldpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false

        } else if (newpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
            false

        } else if (confirmpass.isNullOrBlank()) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false

        } else if (!confirmpass.equals(newpass)) {
            produce(AccountAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false

        } else changePassword(
            ChangPasswordParam( oldpass, newpass , confirmpass

        ))


    }

    fun changePassword(params: ChangPasswordParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(AccountAction.ChangePassword(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }  fun deleteAccount( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(AccountAction.AccountDeleted(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
  fun changePhone(     country_code: String,
                       phone: String,) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AccountAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope , CheckPhoneParam(    country_code ,
                      phone  ,)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AccountAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AccountAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(AccountAction.ShowPhoneUpdated(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(AccountAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


}






