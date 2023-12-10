package com.dotjoo.aghslilnidelivery.ui.fragment.main.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
import com.dotjoo.aghslilnidelivery.databinding.FragmentEditProfileBinding
import com.dotjoo.aghslilnidelivery.util.FileManager
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.loadImage
import com.dotjoo.aghslilnidelivery.util.observe
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    var logo: File? = null
    val mViewModel: AccountViewModel by activityViewModels()

    override fun onFragmentReady() {
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
        onclick()
    }

    private fun onclick() {

        binding.cardGallay.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        binding.btnSignup.setOnClickListener {

            mViewModel.updateProfile(
                UpdateProfileParam(
                    binding.etName.text.toString(),
                    arguments?.getString("CC").toString(),
                    binding.etPhone.text.toString(),
                    logo
                )
            )

        }
    }

    fun handleViewState(action: AccountAction) {
        when (action) {
            is AccountAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AccountAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is AccountAction.ShowProfile -> {

            }

            is AccountAction.ProfileUpdated -> {
                action.msg?.let { showToast(it) }
                findNavController().popBackStack()
            }


            else -> {

            }
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                CropImage.activity(uri)
                    .start(requireContext(), this)
            } else {
            }
        }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                //  val resultUri: Uri = result.uri
                //      image = File(getRealPathFromURI(parent, resultUri))
                result.uri?.let {
                    FileManager.from(requireActivity(), it)?.let { file ->
                        logo = file

                        binding.ivProfile.loadImage(file, isCircular = true)
                    }
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //  val error = result.
            showToast(data?.extras.toString())
        }
    }
}