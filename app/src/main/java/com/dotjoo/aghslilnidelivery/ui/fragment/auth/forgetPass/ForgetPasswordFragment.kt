package com.dotjoo.aghslilnidelivery.ui.fragment.auth.forgetPass

import android.content.Intent
import android.graphics.Paint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.databinding.FragmentForgetPasswordBinding
import com.dotjoo.aghslilnidelivery.ui.activity.AuthActivity
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghslilnidelivery.util.Constants
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title


@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    var state = 1

    private val mViewModel: AuthViewModel by viewModels()
     override fun onFragmentReady() {
        state1()
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.PhoneChecked -> {
                showProgress(false)
                showToast(action.message)
                state2()

            }

            is AuthAction.OtpChecked -> {
                showToast(action.message)
                showProgress(false)
                state3()

            }

            is AuthAction.ResetPasswordSucess -> {
                showToast(action.message)
                showProgress(false)
                findNavController().navigate(R.id.loginFragment)
            }


            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }


    private fun onClick() {
        binding.tvResend.setPaintFlags(binding.tvResend.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.btnEnterNumber.setOnClickListener {
            if (binding.etPhone.text.toString().isNullOrEmpty())
                showToast(resources.getString(R.string.msg_empty_phone_number))
            else {
                mViewModel.email = binding.etPhone.text.toString()
                mViewModel.checkPhone("+${binding.ccp.selectedCountryCode}", mViewModel.email.toString())
            }
        }
        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString().isNullOrEmpty())
                showToast(resources.getString(R.string.msg_empty_otp))
            else {
                mViewModel.otp = binding.etOtp.otp.toString()
                mViewModel.checkOtp(
                    "+${binding.ccp.selectedCountryCode}",
                    mViewModel.email.toString(),
                    mViewModel.otp.toString()
                )
            }
        }
        binding.btnEnterOtp.setOnClickListener {
            state3()
        }
        binding.btnEnterPass.setOnClickListener {
            mViewModel.isValidParamsChangePass(
                "+${binding.ccp.selectedCountryCode}",
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString()
            )
        }
        binding.btnEnterPass.setOnClickListener {
            PrefsHelper.clear()
            var intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            startActivity(intent)
            activity?.finish()
        }
        binding.toolbar.card_back.setOnClickListener {
            if (state == 1) {
                findNavController().navigateUp()
            } else if (state == 2) {
                state1()
            } else {
                state2()
            }
        }
    }


    fun state1() {
        binding.toolbar.tv_title.setText(resources.getText(R.string.forget_password))
        state = 1
        binding.lytState1.visibility = View.VISIBLE
        binding.lytState2.visibility = View.GONE
        binding.lytState3.visibility = View.GONE

    }

    fun state2() {
        //counterDawn ()
        binding.toolbar.tv_title.setText(resources.getText(R.string.otp))
        state = 2
        binding.lytState1.visibility = View.GONE
        binding.lytState2.visibility = View.VISIBLE
        binding.lytState3.visibility = View.GONE

    }

    fun state3() {
        state = 3
        binding.toolbar.tv_title.setText(resources.getText(R.string.new_password))
        binding.lytState1.visibility = View.GONE
        binding.lytState2.visibility = View.GONE
        binding.lytState3.visibility = View.VISIBLE

    }
}