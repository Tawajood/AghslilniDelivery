package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.ui.adapter.NotifactionAdapter
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.databinding.FragmentNotifactionBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title

@AndroidEntryPoint
class NotifactionFragment  : BaseFragment<FragmentNotifactionBinding>() {
    val mViewModel: SettingsViewModel by activityViewModels()

    private val adapter by lazy { NotifactionAdapter() }

    override fun onFragmentReady() {
        onClick()
        initAdapter()
        mViewModel.apply {
            getNotifaction()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getNotifaction()
            binding.swiperefreshHome.isRefreshing = false
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

            is SettingAction.ShowNotifactions -> {
                action.data?.notificationItem?.let {
                    if(it.size>0) {
                        binding.lytEmptyState.isVisible= false
                        adapter.notifactionsItemsList = it
                    }
                    else{
                        binding.lytEmptyState.isVisible= true
                    }
                }
            }

            else -> {

            }
        }
    }
    private fun initAdapter() {
        binding.rvNotifactions.adapter = adapter

    }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.toolbar.tv_title.setText(resources.getString(R.string.notifaction))
        binding.toolbar.card_back.setOnClickListener {
            findNavController().navigateUp()
        }   }

}