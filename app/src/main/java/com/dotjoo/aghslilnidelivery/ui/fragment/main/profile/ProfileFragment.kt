package com.dotjoo.aghslilnidelivery.ui.fragment.main.profile


 import androidx.core.view.isVisible
 import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.data.response.Profile
import com.dotjoo.aghslilnidelivery.databinding.FragmentProfileBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.dialog.ChangeDelteAccountSheetFragment
 import com.dotjoo.aghslilnidelivery.ui.dialog.OnClickLoginFirst
 import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.loadImage
import com.dotjoo.aghslilnidelivery.util.observe
 import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    lateinit var parent: MainActivity
    val mViewModel: AccountViewModel by activityViewModels()
    //var countryCode = "+20"

    var logo : File? = null


    override fun onFragmentReady() {
        onclick()
        mViewModel.apply {
            getProfileData()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getProfileData()
            binding.swiperefreshHome.isRefreshing = false
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

            is AccountAction.ShowProfile ->  {
                action.data.driver.let { setupProfile(it) }

            }
            is AccountAction.ProfileUpdated ->  {
                action.msg.let { showToast(it) }

            }



            else -> {

            }
        }
    }


    private fun setupProfile(profile: Profile) {
        profile?.let {
            binding.etName.setText(it.name)
            binding.ivProfile.loadImage(it.img, isCircular = true)
            binding.etPhone.setText(it.phone)
            binding.ccp.setCountryForPhoneCode(profile.country_code.replace("+", "").toInt())
     binding.lytData.isVisible= true   }
    }


    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(true)
        binding.lytDeletAcc.setOnClickListener {
            ChangeDelteAccountSheetFragment.newInstance(object : OnClickLoginFirst {
                override fun onClick(choice: String) {}
            }).show(childFragmentManager, ChangeDelteAccountSheetFragment::class.java.canonicalName)

        }
        binding.lytChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordSheetFragment)
        }
        if (PrefsHelper.getLanguage()=="en"){
            binding.apply {
                a1.rotationY=180f
                a2.rotationY=180f
            }
        }

        binding.cardEdit.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }


    }



}
