package com.dotjoo.aghslilnidelivery.ui.fragment.auth.spalsh

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.databinding.FragmentSplashBinding
import com.dotjoo.aghslilnidelivery.util.ext.showActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay



class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override fun onFragmentReady() {
        lifecycleScope.launchWhenResumed {
            delay(1500)

                 if (PrefsHelper.getToken().isNullOrEmpty()) { findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )}
                else
                    showActivity(MainActivity::class.java, clearAllStack = true)

        }
    }




    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }

}
