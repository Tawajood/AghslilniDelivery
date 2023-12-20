package com.dotjoo.aghslilnidelivery.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
 import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
 import com.dotjoo.aghslilnidelivery.databinding.DialogCheckOtpBinding
 import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthViewModel
 import com.dotjoo.aghslilnidelivery.util.ToastUtils
import com.dotjoo.aghslilnidelivery.util.ToastUtils.Companion.showToast
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
 import com.dotjoo.aghslilnidelivery.util.observe
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

interface OnPhoneCheckedWithOtp{
    fun onClick(country_code: String,phone: String,verifed: Boolean )
}
@AndroidEntryPoint
class CheckOtpSheetFragment(val country_code: String, val phone: String , val onClick:OnPhoneCheckedWithOtp ) :  BottomSheetDialogFragment() {

    private lateinit var binding: DialogCheckOtpBinding
    private lateinit var parent: MainActivity
    val mViewModel: AuthViewModel by viewModels()
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var phoneAuthProvider: PhoneAuthProvider.ForceResendingToken
    var verifiedOtp =""
    var otp =""
    companion object {
        fun newInstance(country_code: String, phone: String,onClick:OnPhoneCheckedWithOtp  ): CheckOtpSheetFragment {
            val args = Bundle()
            val f = CheckOtpSheetFragment(country_code ,phone , onClick
            )
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogCheckOtpBinding.inflate(inflater)
        sendVerfaction(country_code+phone)
        counterDawn()
        onClick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }

        }

        return binding.root
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

    private fun sendVerfaction(phoneNumber:String) {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                 showToast(requireContext(),p0.message.toString())
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                showToast(requireContext(),p0)
                verifiedOtp=p0
                phoneAuthProvider=p1
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
        var credential= PhoneAuthProvider.getCredential(verifiedOtp, otp)
        signin(credential)

    }

    private fun signin(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                if (it.isSuccessful) {
                    Log.i("PhoneAuthCredential","sucess")
                    onClick.onClick(country_code, phone, true)
                    dismiss()
                }else{
                    showToast(requireContext(),resources.getString(R.string.wrongotp))
                    Log.i("PhoneAuthCredential","false")

                }
            }

        })

    }


    fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    ToastUtils.showToast(requireContext(), action.message)
                    showProgress(false)
                }
            }




            else -> {

            }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible= show
    }


    private fun onClick() {
        binding.btnEnterOtp.setOnClickListener {
            if (binding.etOtp.otp.toString()
                    .isNullOrEmpty()
            ) showToast(requireContext(), resources.getString(R.string.msg_empty_otp))
            else {
                otp = binding.etOtp.otp.toString()
                verfyOtp()
            }
        }
        binding.tvResend.setOnClickListener {
            sendVerfaction(country_code+mViewModel.phone)
            counterDawn()
        }
    }


    @SuppressLint("CutPasteId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet!!)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme


}