package com.dotjoo.aghslilnidelivery.ui.fragment.main.moreFragments

import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.databinding.FragmentWalletBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding>() {
    override fun onFragmentReady() {
        onClick()   }

    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
    }

}