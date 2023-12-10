package com.dotjoo.aghslilnidelivery.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dotjoo.aghslilnidelivery.util.ext.bindView
import com.dotjoo.aghslilnidelivery.util.ext.castToActivity
import com.dotjoo.aghslilnidelivery.util.ext.showAppToast

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    open fun mPageTitle(): String? = null
    /**
     * first: background res
     * second: height
     * */

    open fun mPageToolbarBackground(): Pair<Int, Int>? = null

     open val mTitleGravity: Int = Gravity.CENTER_VERTICAL
     open val showBottomNavigationView: Boolean = true

    abstract fun onFragmentReady()


    private var _binding: B? = null
    open lateinit var binding: B
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView()
        binding = _binding!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //   setToolbarTitle(mPageTitle())
        onFragmentReady()
    }

    override fun onResume() {
        super.onResume()
        showProgress(false)

    }
/*
    fun showProgress(show: Boolean) {

   *//*     if (show == false) loadingUtil.hideLoading()
        else loadingUtil.showLoading()*//*


    }*/

    fun showProgress(show: Boolean = true) {
        castToActivity<BaseActivity<*>> {
            it?.baseShowProgress?.set(show)
        }
    }

    fun showToast(message: String?) {
        context?.showAppToast(message)
    }

    override fun onDestroyView() {
        showProgress(false)
      //  loadingUtil.hideLoading()
        _binding = null
        super.onDestroyView()
    }


}
