package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.databinding.FragmentContactUsBinding
 import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {
    lateinit var parent: MainActivity
    val mViewModel: SettingsViewModel by activityViewModels()
    override fun onFragmentReady() {
        mViewModel.apply {
            showContact()
            observe(viewState) {
                handleViewState(it)
            }

        }
        onClick()
    }

    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.tv_title.setText(resources.getText(R.string.contact_us))
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.lytIt.setOnClickListener {
            findNavController().navigate(R.id.itFragment)
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

                }else if (it.contains("aghsilini.com") == true) {
                    showToast( resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowContact -> {
                binding.tvPhone.text=action.data?.phone
                binding.tvEmail.text=action.data?.email
            }

            else -> {

            }
        }
    }

}