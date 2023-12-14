package com.dotjoo.aghslilnidelivery.ui.fragment.main.more

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.dotjoo.aghslilnidelivery.ui.fragment.main.profile.AccountAction
import com.dotjoo.aghslilnidelivery.ui.fragment.main.profile.AccountViewModel
import com.dotjoo.aghslilnidelivery.util.Constants
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.showActivity
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>() {
    val mViewModel: AccountViewModel by activityViewModels()
    lateinit var parent: MainActivity

    override fun onFragmentReady() {
        mViewModel.apply {
            getProfileData()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getProfileData()
            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }
        onClick()
    }


    @SuppressLint("SetTextI18n")
    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401")) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ShowProfile -> {
                action.data.driver.let {
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.CEILING
                    binding.tvTotalValue.text =
                        df.format(it.total).toString() + " " + getString(R.string.sr)
                    binding.tvFavValue.text =
                        df.format(it.balance).toString() + " " + getString(R.string.sr)
                    binding.lytBalanceDetails.isVisible = true
                }

            }

            is AccountAction.ProfileUpdated -> {
                action.msg.let { showToast(it) }

            }


            else -> {

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClick() {
        binding.tvLogout.paintFlags = binding.tvLogout.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        if (PrefsHelper.getLanguage() == Constants.AR) {
            binding.tvLang.text = "EN"
        } else {
            binding.tvLang.text = "AR"
        }

        binding.tvContactus.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
        binding.tvAboutUs.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }
        binding.tvSetting.setOnClickListener {
            if (PrefsHelper.getLanguage() == Constants.EN) {
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
            val intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            activity?.startActivity(intent)
            activity?.finish()
        }

    }

}