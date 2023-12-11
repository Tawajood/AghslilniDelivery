package com.dotjoo.aghslilnidelivery.ui.fragment.main.more

import android.content.Intent
import android.graphics.Paint
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.databinding.FragmentMoreBinding
import com.dotjoo.aghslilnidelivery.ui.activity.AuthActivity
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.dialog.BalanceWithdrawSheetFragment
import com.dotjoo.aghslilnidelivery.ui.dialog.ChangeDelteAccountSheetFragment
import com.dotjoo.aghslilnidelivery.ui.dialog.OnClickLoginFirst
import com.dotjoo.aghslilnidelivery.util.Constants
import com.dotjoo.aghslilnidelivery.util.ext.showActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {

    override fun onFragmentReady() {
        onClick()   }

    lateinit var parent: MainActivity
    private fun onClick() {
        binding.tvLogout.setPaintFlags(binding.tvLogout.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        if (PrefsHelper.getLanguage()=="en") {
            binding.tvLang.text="EN"
        }else{
            binding.tvLang.text="AR"
        }

        binding.tvContactus.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }
        binding.tvSetting.setOnClickListener {
            if (PrefsHelper.getLanguage()=="en") {
                PrefsHelper.setLanguage(Constants.AR)

                showActivity(MainActivity::class.java, clearAllStack = true)
            } else {
                PrefsHelper.setLanguage(Constants.EN)
                showActivity(MainActivity::class.java, clearAllStack = true)
            }
        }
        binding.tvTerms.setOnClickListener {
            findNavController().navigate(R.id.termsFragment)
        }
        binding.tvNotifaction.setOnClickListener {
            findNavController().navigate(R.id.notifactionFragment)
        }
        binding.tvIt.setOnClickListener {
            findNavController().navigate(R.id.itFragment)
        }
        binding.tvWithdraw.setOnClickListener {
                BalanceWithdrawSheetFragment.newInstance(object : OnClickLoginFirst {
                    override fun onClick(choice: String) {}
                }).show(childFragmentManager, BalanceWithdrawSheetFragment::class.java.canonicalName)


        }

        binding.tvLogout.setOnClickListener {
            PrefsHelper.clear()
            var intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            activity?. startActivity(intent)
            activity?.finish()
        }

    }

}