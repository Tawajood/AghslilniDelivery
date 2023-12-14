package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

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
import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
import com.dotjoo.aghslilnidelivery.data.param.WithdrawPrams
import com.dotjoo.aghslilnidelivery.data.response.AboutResponse
import com.dotjoo.aghslilnidelivery.data.response.ChatResponse
import com.dotjoo.aghslilnidelivery.data.response.ContactResponse
import com.dotjoo.aghslilnidelivery.data.response.NotifactionResponse
import com.dotjoo.aghslilnidelivery.data.response.ProfileResponse
import com.dotjoo.aghslilnidelivery.domain.AccountUseCase
import com.dotjoo.aghslilnidelivery.domain.SettingsUseCase
import com.dotjoo.aghslilnidelivery.ui.fragment.main.profile.AccountAction

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel
@Inject constructor(
    app: Application,
    val useCase: SettingsUseCase
) : BaseViewModel<SettingAction>(app) {

    fun getNotifaction() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,2
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowNotifactions(res.data?.data as NotifactionResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getMessages() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,1
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAllMessage(res.data?.data as ChatResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun sendMessages(pram:String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,pram
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            //produce(SettingAction.ShowAllMessage(res.data?.data as Any))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun showAbout() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,3
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAbout(res.data?.data as AboutResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun showContact() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,4
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowContact(res.data?.data as ContactResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun showTerms() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,5
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.ShowAbout(res.data?.data as AboutResponse))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun isVaildWithdraw(amount: String, bank_name: String, account_number: String) {
        if (amount.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.msg_empty_amount)))
            false

        } else if (bank_name.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.msg_empty_bank_name)))
            false

        } else if (account_number.isNullOrBlank()) {
            produce(SettingAction.ShowFailureMsg(getString(R.string.empty_account_number)))
            false

        } else withdraw(WithdrawPrams(
            amount, bank_name, account_number
        ))


    }
    fun withdraw(prams:WithdrawPrams) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(SettingAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,prams
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(SettingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(SettingAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(SettingAction.Withdraw(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(SettingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
}






