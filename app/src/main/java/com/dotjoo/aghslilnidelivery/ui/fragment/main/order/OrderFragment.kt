package com.dotjoo.aghslilnidelivery.ui.fragment.main.order

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.response.Order
import com.dotjoo.aghslilnidelivery.databinding.FragmentOrderBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderAdapter
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.CURRNET
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.FINISHED
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.NEW
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderAction
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderViewModel
import com.dotjoo.aghslilnidelivery.ui.lisener.OnOrderClickListener
import com.dotjoo.aghslilnidelivery.util.Resource
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.init
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
    class OrderFragment : BaseFragment<FragmentOrderBinding>()
        , OnOrderClickListener {
        lateinit var adapter: OrderAdapter
        val mViewModel: OrderViewModel by activityViewModels()
        var listNew = arrayListOf<Order>()
        var listCurrent = arrayListOf<Order>()
        var listPrev = arrayListOf<Order>()
        override fun onFragmentReady() {
            initAdapters()
            onClick()


            mViewModel.apply {
                 observe(viewState) {
                    handleViewState(it)
                }

            }
            binding.swiperefreshHome.setOnRefreshListener {
                mViewModel.getCurrentOrder()
                mViewModel.getNewOrders("29.8494216","31.3441353"  )
                 //mViewModel.getAllLaundries(lat,lang)
                if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
            }
        }

    fun handleViewState(action: OrderAction) {
        when (action) {
            is OrderAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is OrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is OrderAction.NewOrders -> {
                listNew= action.data.orders
           loadLaundries(listNew, NEW)

            }
      is OrderAction.CurrentOrders -> {
                listCurrent= action.data.orders

            }
            is OrderAction.PrevOrders->{
                listPrev = action.data.orders
            }


            else -> {

            }
        }
    }

        lateinit var parent: MainActivity
        private fun onClick() {
            binding.toolbar.card_back.isVisible = false
            parent = requireActivity() as MainActivity
            parent.showBottomNav(true)
        }

        private fun initAdapters() {
            binding.toolbar.tv_title.setText(resources.getString(R.string.orders))
            adapter = OrderAdapter(this)
            binding.rvOrders.init(requireContext(), adapter, 2)


            setupClick()
          //  observeData()
            loadLaundries(listNew, NEW)
            // loadLaundries(listCurrent, CURRNET)
            //  loadLaundries(listPrev, FINISHED)


        }

        private fun setupClick() {
            binding.titleCurrent.setOnClickListener {
                stateCurrent()
            }
            binding.titleFinished.setOnClickListener {
                stateFinished()
            }
            binding.titleNew.setOnClickListener {
                stateNew()
            }
        }

        private fun observeData() {
            lifecycleScope.launch {
                mViewModel.new.collectLatest {
                    //      binding.refresher.isRefreshing = false
                    when (it) {
                        is Resource.Failure -> {
                            if (it.message?.contains("401") == true) {
                                findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                            } else {
                                showToast(it.message)
                                showProgress(false)
                            }
                        }

                        is Resource.Progress -> {

                            showProgress(it.loading)
                            if (it.loading) {
                                hideKeyboard()
                            }
                        }

                        is Resource.Success -> {
                            listNew = it.data as ArrayList<Order>
                            adapter.ordersList = listNew
                            loadLaundries(listNew, NEW)

                        }
                    }
                }

                mViewModel.current.collectLatest {
                    //      binding.refresher.isRefreshing = false
                    when (it) {
                        is Resource.Failure -> {
                            if (it.message?.contains("401") == true) {
                                findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                            } else {
                                showToast(it.message)
                                showProgress(false)
                            }
                        }

                        is Resource.Progress -> {

                            showProgress(it.loading)
                            if (it.loading) {
                                hideKeyboard()
                            }
                        }

                        is Resource.Success -> {
                            listCurrent = it.data as ArrayList<Order>

                        //    adapter.ordersList = arrayListOf()
                            // loadLaundries(listNew, CURRNET)
                        }
                    }
                }
            }
        }

        private fun stateCurrent() {
            binding.titleCurrent.background = resources.getDrawable(R.drawable.bg_blue)
            binding.titleNew.background = resources.getDrawable(R.color.white)
            binding.titleFinished.background = resources.getDrawable(R.color.white)
            binding.titleCurrent.setTextColor(resources.getColor(R.color.white))
            binding.titleNew.setTextColor(resources.getColor(R.color.blue))
            binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
            loadLaundries(listCurrent, CURRNET)


        }

        private fun stateNew() {
            binding.titleCurrent.background = resources.getDrawable(R.color.white)
            binding.titleNew.background = resources.getDrawable(R.drawable.bg_blue)
            binding.titleFinished.background = resources.getDrawable(R.color.white)
            binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
            binding.titleNew.setTextColor(resources.getColor(R.color.white))
            binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
            loadLaundries(listNew, NEW)
            // adapter.ordersList = arrayListOf()
            //   loadLaundries(list, NEW)
        }

        private fun stateFinished() {
            binding.titleCurrent.background = resources.getDrawable(R.color.white)
            binding.titleNew.background = resources.getDrawable(R.color.white)
            binding.titleFinished.background = resources.getDrawable(R.drawable.bg_blue)
            binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
            binding.titleNew.setTextColor(resources.getColor(R.color.blue))
            binding.titleFinished.setTextColor(resources.getColor(R.color.white))
            //  adapter.ordersList = arrayListOf()
              loadLaundries(listPrev, FINISHED)
        }

        private fun loadLaundries(list: ArrayList<Order>, type: Int) {
            if (list.size > 0) {
                binding.lytEmptyState.isVisible = false
                adapter.ordersList = list
                adapter.type = type
                adapter.notifyDataSetChanged()
            } else {
                adapter.ordersList = list
                adapter.type = type
                adapter.notifyDataSetChanged()
                when (type) {

                    NEW -> {
                        binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_new))
                    }

                    CURRNET -> {
                        binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_current))
                    }

                    FINISHED -> {
                        binding.tvMsgNoLaundries.setText(resources.getString(R.string.no_orders_before))

                    }

                }
                binding.lytEmptyState.isVisible = true
            }

        }

        override fun onItemsClickLisener(item: Order) {
            mViewModel.orderId = item.id

     findNavController().navigate(R.id.orderInfoFragment)
        }

    }
