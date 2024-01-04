package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments


import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.databinding.FragmentAboutBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    val mViewModel: SettingsViewModel by activityViewModels()
    lateinit var parent: MainActivity

    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            showAbout()
            observe(viewState) {
                handleViewState(it)
            }

        }
    }

    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
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

                } else if (it.contains("aghsilini.com") == true) {
                    showToast( resources.getString(R.string.connection_error))
                }else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAbout -> {
                binding.tvDesc.text=action.data?.about!!.body
            }

            else -> {

            }
        }
    }

}