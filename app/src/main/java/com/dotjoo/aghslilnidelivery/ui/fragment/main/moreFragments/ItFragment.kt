package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

import android.text.TextUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.response.Message
import com.dotjoo.aghslilnidelivery.databinding.FragmentItBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.adapter.MessagesAdapter
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItFragment : BaseFragment<FragmentItBinding>() {
    private val adapter by lazy { MessagesAdapter() }
    private var messages = mutableListOf<Message>()
    val mViewModel: SettingsViewModel by activityViewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getMessages()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {

            mViewModel.getMessages()

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

                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAllMessage -> {
                action.data?.messages?.toMutableList()?.let {
                    messages = it
                    adapter.messages = messages
                    binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
                }
            }

            else -> {

            }
        }
    }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.messagesRv.adapter = adapter
        binding.cardClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.cardSend.setOnClickListener {
            if (!TextUtils.isEmpty(binding.etMessage.text)) {
                mViewModel.sendMessages(binding.etMessage.text.toString())
                adapter.addMessage(
                    Message(
                        binding.etMessage.text.toString(),
                        "send"
                    )
                )


                binding.etMessage.setText("")
                binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
            }
        }
    }
}