package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

import com.dotjoo.aghslilnidelivery.base.Action
import com.dotjoo.aghslilnidelivery.data.response.AboutResponse
import com.dotjoo.aghslilnidelivery.data.response.ChatResponse
import com.dotjoo.aghslilnidelivery.data.response.ContactResponse
import com.dotjoo.aghslilnidelivery.data.response.NotifactionResponse

sealed class SettingAction:Action {
    data class ShowLoading(val show: Boolean) : SettingAction()
    data class ShowFailureMsg(val message: String?) : SettingAction()
    data class ShowNotifactions(val data: NotifactionResponse?) : SettingAction()
    data class ShowAllMessage(val data: ChatResponse?) : SettingAction()
    data class ShowAbout(val data: AboutResponse?) : SettingAction()
    data class ShowTerms(val data: AboutResponse?) : SettingAction()
    data class ShowContact(val data: ContactResponse?) : SettingAction()
    data class Withdraw(val message: String?) : SettingAction()

}