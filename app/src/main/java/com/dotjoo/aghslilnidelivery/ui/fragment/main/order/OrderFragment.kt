package com.dotjoo.aghslilnidelivery.ui.fragment.main.order

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.data.response.Order
import com.dotjoo.aghslilnidelivery.databinding.FragmentOrderBinding
import com.dotjoo.aghslilnidelivery.fcm.FcmBroadcast
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderAdapter
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.CURRNET
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.FINISHED
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderType.NEW
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderAction
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderViewModel
import com.dotjoo.aghslilnidelivery.ui.lisener.OnOrderClickListener
import com.dotjoo.aghslilnidelivery.util.PermissionManager
import com.dotjoo.aghslilnidelivery.util.WWLocationManager
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.init
import com.dotjoo.aghslilnidelivery.util.observe
import com.dotjoo.aghslilnidelivery.util.openLocationSettingsResultLauncher
import com.dotjoo.aghslilnidelivery.util.requestAppPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.card_back
import kotlinx.android.synthetic.main.toolbar.view.tv_title
import javax.inject.Inject


@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>(), OnOrderClickListener {
    lateinit var adapter: OrderAdapter
    val mViewModel: OrderViewModel by activityViewModels()
    var listNew = arrayListOf<Order>()
    var listCurrent = arrayListOf<Order>()
    var listPrev = arrayListOf<Order>()
    var state = NEW

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var locationManager: WWLocationManager
    private val fcmBroadcast by lazy { FcmBroadcast() }
    private val reciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkLocation()

        }
    }

    override fun onFragmentReady() {
        setupUi()
        initAdapters()
        setupClick()
        context?.registerReceiver(reciever, IntentFilter(MainActivity.MAIN_SCREEN_ACTION))
        checkLocation()

        mViewModel.apply {
            getProfileData()

            getPrevOrder()
            getCurrentOrder()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            if (state == NEW) checkLocation()
            else if (state == FINISHED) mViewModel.getPrevOrder()
            else mViewModel.getCurrentOrder()
            mViewModel.getProfileData()
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
                } else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is OrderAction.NewOrders -> {
                listNew = action.data.orders
                if (state == NEW) stateNew()

            }

            is OrderAction.CurrentOrders -> {
                listCurrent = action.data.orders
                if (state == CURRNET) stateCurrent()

            }

            is OrderAction.PrevOrders -> {
                listPrev = action.data.orders
                if (state == FINISHED) stateFinished()
            }

            is OrderAction.ShowProfile -> {
binding.checkbox.isVisible= true
                binding.checkbox.isChecked = action.data?.driver?.activation == true
                action.data.driver.let { PrefsHelper.saveUserData(it) }
            }

            is OrderAction.ShowActivation -> {
                mViewModel.getProfileData()
            }

            else -> {

            }
        }
    }

    lateinit var parent: MainActivity
    private fun setupUi() {
         parent = requireActivity() as MainActivity
        binding.tvTitle.setText(resources.getString(R.string.orders))
        parent.showBottomNav(true)
    }

    private fun initAdapters() {
        adapter = OrderAdapter(this)
        binding.rvOrders.init(requireContext(), adapter, 2)
        loadLaundries(listNew, NEW)
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
        binding.checkbox.setOnClickListener {
            if (binding.checkbox.isChecked) mViewModel.changeActivationMode(1)
            else mViewModel.changeActivationMode(0)

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
        state = CURRNET

    }

    private fun stateNew() {
        binding.titleCurrent.background = resources.getDrawable(R.color.white)
        binding.titleNew.background = resources.getDrawable(R.drawable.bg_blue)
        binding.titleFinished.background = resources.getDrawable(R.color.white)
        binding.titleCurrent.setTextColor(resources.getColor(R.color.blue))
        binding.titleNew.setTextColor(resources.getColor(R.color.white))
        binding.titleFinished.setTextColor(resources.getColor(R.color.blue))
        loadLaundries(listNew, NEW)
        state = NEW
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
        state = FINISHED
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


    private fun checkLocation() {
        if (permissionManager.hasAllLocationPermissions()) {
            checkIfLocationEnabled()
        } else {
            permissionsLauncher?.launch(permissionManager.getAllLocationPermissions())
        }
    }


    private val permissionsLauncher = requestAppPermissions { allIsGranted, _ ->
        if (allIsGranted) {
            checkIfLocationEnabled()
        } else {
            Toast.makeText(
                activity, getString(R.string.not_all_permissions_accepted), Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun checkIfLocationEnabled() {
        /////////////////////////////////////////  request()
        if (locationManager.isLocationEnabled()) {

            request()


        } else {
            Log.d("location", "NoisLocationEnabled")
            activity?.let {
                locationManager.showAlertDialogButtonClicked(
                    it, locationSettingLauncher
                )
            }
        }
    }

    private fun request() {
        locationManager.getLastKnownLocation { location ->
            location.latitude?.let {

                mViewModel.getNewOrders(it.toString(), location.longitude.toString())

            }
        }


    }

    private val locationSettingLauncher = openLocationSettingsResultLauncher {
        checkIfLocationEnabled()
    }

}
