package com.dotjoo.aghslilnidelivery.ui.fragment.auth.forgetPass

import android.content.Intent
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>(),
    CountryCodePicker.OnCountryChangeListener {
    var state = 1
    lateinit var phoneAuthProvider: PhoneAuthProvider.ForceResendingToken
    var verifiedOtp =""
    var otp =""
    var countryCode = "+966"
    private var auth: FirebaseAuth = Firebase.auth
    //  private var userAccount: String? = null
    //   var firebaseAuthSettings: FirebaseAuthSettings = auth.getFirebaseAuthSettings()
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val mViewModel: AuthViewModel by viewModels()
     override fun onFragmentReady() {
        state1()
        onClick()
         onBack()
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
        binding.ccp.setOnCountryChangeListener(this)
        binding.tvResend.setPaintFlags(binding.tvResend.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.btnEnterNumber.setOnClickListener {
            if (binding.etPhone.text.toString().isNullOrEmpty())
                showToast(resources.getString(R.string.msg_empty_phone_number))
            else {
                mViewModel.phone = binding.etPhone.text.toString()
                mViewModel.checkPhone("+${binding.ccp.selectedCountryCode}", mViewModel.phone.toString())
            }
        }


        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString()
                    .isNullOrEmpty()
            ) showToast(resources.getString(R.string.msg_empty_otp))
            else {
                otp= binding.etOtp.otp.toString()
                 verfyOtp()

            }
        }
        binding.btnEnterPass.setOnClickListener {
            mViewModel.isValidParamsChangePass(
                "+${binding.ccp.selectedCountryCode}",
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString()
            )
        }
        binding.tvResend.setOnClickListener {
            sendVerfaction(countryCode+mViewModel.phone)
            state2()
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



private fun sendVerfaction(phoneNumber:String) {
    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            showToast("com")
         }

        override fun onVerificationFailed(p0: FirebaseException) {
             showToast(p0.message)
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            showToast(p0)
            verifiedOtp=p0
            phoneAuthProvider=p1    //   fragmentInputOtpBinding?.btnGetOtp?.visibility = View.VISIBLE
            //     moveToVerifyOtp(p0)
        }
    }

    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber) // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(requireActivity()) // Activity (for callback binding)
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)    }

private fun verfyOtp() {
    var credential=PhoneAuthProvider.getCredential(verifiedOtp, otp)
    signin(credential)
    val multiFactorAssertion
            = PhoneMultiFactorGenerator.getAssertion(credential)

}

private fun signin(credential: PhoneAuthCredential) {
    auth.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener {
        if (it.isSuccessful) {
            if (it.isSuccessful) {
                Log.i("PhoneAuthCredential","sucess")
                state3()
            }else{
                showToast(resources.getString(R.string.wrongotp))
                Log.i("PhoneAuthCredential","false")

            }
        }

    })

}
private fun onBack() {
    activity?.let {
        requireActivity().onBackPressedDispatcher.addCallback(
            it,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (state == 1) {
                        if (isEnabled) {
                            isEnabled = false
                            findNavController().navigateUp()
                        }
                    } else if (state == 2) {
                        state1()
                    } else {
                        state2()
                    }

                }
            })
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
         counterDawn ()
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

    private var restTimer: CountDownTimer? = null
    private fun counterDawn() {
        binding.tvResend.isEnabled = false
        binding.tvResend.setTextColor(resources.getColor(R.color.grey_400))
        binding.tvTimer.setTextColor(resources.getColor(R.color.grey_400))

        restTimer = object : CountDownTimer(120000, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished / 1000 % 60
                val minutes: Long = (millisUntilFinished - seconds) / 1000 / 60
                binding.tvTimer.text = "" + minutes + ":" + seconds
                Log.d(
                    "remainingremaining g", ("" + minutes + ":" + seconds).toString()
                )
            }


            override fun onFinish() {
                binding.tvResend.setText(resources.getString(R.string.resend))
                binding.tvTimer.text = ""
                binding.tvResend.isEnabled = true
                binding.tvResend.setTextColor(resources.getColor(R.color.black))


            }
        }.start()
    }
    override fun onCountrySelected() {
        countryCode = "+" + binding.ccp.selectedCountryCode
    }
}