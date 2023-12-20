package com.dotjoo.aghslilnidelivery.ui.fragment.auth.register

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.databinding.FragmentRegisterBinding
import com.dotjoo.aghslilnidelivery.ui.activity.AuthActivity
import com.dotjoo.aghslilnidelivery.ui.dialog.CheckOtpSheetFragment
import com.dotjoo.aghslilnidelivery.ui.dialog.OnPhoneCheckedWithOtp
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghslilnidelivery.util.FileManager
import com.dotjoo.aghslilnidelivery.util.PermissionManager
import com.dotjoo.aghslilnidelivery.util.ToastUtils
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.loadImage
import com.dotjoo.aghslilnidelivery.util.observe
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private var type: Int = -1
    lateinit var parent: AuthActivity
    private val mViewModel: AuthViewModel by viewModels()
    var verified_countryCode = ""
    var verified_phone: String? = null
    var file_id: File? = null
    var file_driver_lis: File? = null
    var file_car_lisence: File? = null
    var file_profile_img: File? = null

    @Inject
    lateinit var permissionManager: PermissionManager
    override fun onFragmentReady() {
        onClick()
        loadImages()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun loadImages() {
        file_id?.let { binding.ivId.loadImage(it, isCircular = true) }
        file_driver_lis?.let { binding.ivDrivingLisence.loadImage(it, isCircular = true) }
        file_car_lisence?.let { binding.ivCarLisence.loadImage(it, isCircular = true) }
        file_profile_img?.let { binding.ivProfileImg.loadImage(it, isCircular = true) }
    }

    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                findNavController().navigate(R.id.waitingActivationFragment)
                findNavController().navigate(
                    R.id.waitingActivationFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
                )
            }

            is AuthAction.ShowRegisterVaildationSucess -> {
                showProgress(false)
                if (verified_phone.isNullOrEmpty() || verified_phone == null) {
                    CheckOtpSheetFragment.newInstance(action.param.country_code,
                        action.param.phone,
                        object : OnPhoneCheckedWithOtp {
                            override fun onClick(
                                country_code: String, phone: String, verifed: Boolean
                            ) {
                                verified_phone = phone
                                verified_countryCode = country_code
                                mViewModel.register(action.param)
                            }
                        }).show(
                        childFragmentManager, "CheckOtpSheetFragment"
                    )

                } else {
                    if (verified_phone == action.param.phone && verified_countryCode == action.param.country_code) {
                        mViewModel.register(action.param)
                    } else {
                        CheckOtpSheetFragment.newInstance(action.param.country_code,
                            action.param.phone,
                            object : OnPhoneCheckedWithOtp {
                                override fun onClick(
                                    country_code: String, phone: String, verifed: Boolean
                                ) {
                                    verified_phone = phone
                                    verified_countryCode = country_code
                                    mViewModel.register(action.param)
                                }
                            }).show(
                            childFragmentManager, "CheckOtpSheetFragment"
                        )
                    }
                }
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
        parent = requireActivity() as AuthActivity
        binding.toolbar.tv_title.setText(resources.getString(R.string.new_user))
        binding.tvTermsandcondito.setPaintFlags(binding.tvTermsandcondito.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvTermsandcondito.setOnClickListener {
            findNavController().navigate(R.id.termsFragment2)

        }

        binding.btnSignup.setOnClickListener {
            if (file_id == null || file_car_lisence == null || file_driver_lis == null) {
                ToastUtils.showToast(
                    requireContext(),
                    getString(R.string.please_attach_the_car_papers_and_license)
                )
            } else {
                mViewModel.isVaildRegisteration(
                    binding.etName.text.toString(),
                    "+${binding.ccp.selectedCountryCode}",
                    binding.etPhone.text.toString(),
                    file_car_lisence, file_driver_lis, file_id, file_profile_img,
                    binding.etPassword.text.toString(),
                    binding.etPasswordConfim.text.toString(),
                )
            }

        }
        binding.btnSignin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)

        }

        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cardImgCarLisence.setOnClickListener {
            type = 0
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.cardImgId.setOnClickListener {
            type = 2
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.cardImgDrivingLisence.setOnClickListener {
            type = 1
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.cardProfileimg.setOnClickListener {
            type = 3
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
    }



    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                CropImage.activity(uri).start(requireContext(), this)
            } else {
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                        if (type == 0) {
                            file_car_lisence = file
                            binding.ivCarLisence.loadImage(file, isCircular = true)
                            //      binding.ivProfile.loadImage(file, isCircular = true)
                        } else if (type == 1) {
                            file_driver_lis = file
                            binding.ivDrivingLisence.loadImage(file, isCircular = true)

                        } else if (type == 2) {
                            binding.ivId.loadImage(file, isCircular = true)
                            file_id = file

                        } else {
                            binding.ivProfileImg.loadImage(file, isCircular = true)
                            file_profile_img = file

                        }
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //  val error = result.
                showToast(data?.extras.toString())
            }
        }
    }

}
