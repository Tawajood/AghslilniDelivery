package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments


import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
 import com.dotjoo.aghslilnidelivery.databinding.FragmentTermsBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
@AndroidEntryPoint
class TermsFragment   : BaseFragment<FragmentTermsBinding>() {
    lateinit var parent: MainActivity
    val mViewModel: SettingsViewModel by activityViewModels()

    override fun onFragmentReady() {
        mViewModel.apply {
            showTerms()
            showAbout()
            observe(viewState) {
                handleViewState(it)
            }

        }
        onClick()
    }

    private fun onClick() {
     try {
         parent = requireActivity() as MainActivity
         parent.showBottomNav(false)
         binding.toolbar.card_back.setOnClickListener {
             findNavController().navigateUp()
         }
     }  catch (e:Exception){

     }
    }
    fun handleViewState(action: SettingAction) {
        when (action) {
            is SettingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is SettingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowTerms -> {
                //binding.tvDesc.text=action.data?.about!!.body
            }
            is SettingAction.ShowAbout -> {
                binding.tvDesc.text=action.data?.about!!.body
            }

            else -> {

            }
        }
    }

}