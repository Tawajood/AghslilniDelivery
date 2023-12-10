package com.dotjoo.aghslilnidelivery.ui.fragment.auth.register

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.databinding.FragmentRegisterBinding
import com.dotjoo.aghslilnidelivery.ui.activity.AuthActivity
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthAction
import com.dotjoo.aghslilnidelivery.ui.fragment.auth.login.AuthViewModel
import com.dotjoo.aghslilnidelivery.util.FileManager
import com.dotjoo.aghslilnidelivery.util.PermissionManager
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.loadImage
import com.dotjoo.aghslilnidelivery.util.ext.showActivity
import com.dotjoo.aghslilnidelivery.util.observe
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.iv_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private var type: Int = -1
    lateinit var parent: AuthActivity
    private val mViewModel: AuthViewModel by viewModels()
    var countryCode = "+20"
    var file_id: File? = null
    var file_driver_lis: File? = null
    var file_car_lisence: File? = null

    @Inject
    lateinit var permissionManager: PermissionManager
    override fun onFragmentReady() {
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

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                //  action.data.client?.social= action.social
                PrefsHelper.saveUserData(action.data)
                PrefsHelper.saveToken(action.data.token)
                goHome()

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


        binding.btnSignup.setOnClickListener {
            mViewModel.isVaildRegisteration(
                binding.etName.text.toString(),
                countryCode,
                binding.etPhone.text.toString(),
                file_car_lisence, file_driver_lis, file_id,
                binding.etPassword.text.toString(),
                binding.etPasswordConfim.text.toString(),
            )
        }
        binding.btnSignin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)

        }

        binding.toolbar.iv_back.setOnClickListener {
            activity?.finish()
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
    }

    fun goHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

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
                            binding.ivId.loadImage(file , isCircular = true)
                            file_id = file

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
