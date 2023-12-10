package com.dotjoo.aghslilnidelivery

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {

    companion object CompanionObject {

        //   lateinit var dataStore: DataStore<Preferences>
    }

    /*
         override fun attachBaseContext(newBase: Context) {
            val locale = Locale(Constants.EN)
            val localeUpdatedContext: Context = updateLocale(newBase, locale)
             super.attachBaseContext(LocaleManager.setLocale(base));
             super.attachBaseContext(localeUpdatedContext)
        }*/

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //  dataStore = createDataStore(name = "settings")
        PrefsHelper.init(applicationContext)

    }
}