package com.dotjoo.aghslilnidelivery.base
import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface Action

open class BaseViewModel <T : Action>(val app: Application) : AndroidViewModel(app){
    private val nextAction = MutableSharedFlow<T>()
    val viewState: SharedFlow<T> = nextAction


    /*fun isLoggedIn(sessionManager: SessionManager): Boolean {
        return sessionManager.getTokenBlocking()?.isNotBlank() ?: false
    }*/
    fun produce(t: T) {
        viewModelScope.launch(Dispatchers.Main) {
            nextAction.emit(t)
        }
    }
    fun getString(@StringRes stringRes: Int): String {
        return app.getString(stringRes)
    }
}
