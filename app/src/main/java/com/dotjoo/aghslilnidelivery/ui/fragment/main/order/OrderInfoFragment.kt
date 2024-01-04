package com.dotjoo.aghslilnidelivery.ui.fragment.main.order

 import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseFragment
import com.dotjoo.aghslilnidelivery.data.response.OrderInfoItem
import com.dotjoo.aghslilnidelivery.data.response.OrderInfoResponse
import com.dotjoo.aghslilnidelivery.databinding.FragmentOrderInfoBinding
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.ui.adapter.OrderInfoItemsAdapter
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderAction
import com.dotjoo.aghslilnidelivery.ui.fragment.main.home.OrderViewModel
import com.dotjoo.aghslilnidelivery.util.Constants
import com.dotjoo.aghslilnidelivery.util.SimpleDividerItemDecoration
import com.dotjoo.aghslilnidelivery.util.ext.hideKeyboard
import com.dotjoo.aghslilnidelivery.util.ext.init
import com.dotjoo.aghslilnidelivery.util.ext.loadImage
import com.dotjoo.aghslilnidelivery.util.ext.openMap
import com.dotjoo.aghslilnidelivery.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderInfoFragment : BaseFragment<FragmentOrderInfoBinding>() {
    val mViewModel: OrderViewModel by activityViewModels()
    lateinit var adapter: OrderInfoItemsAdapter
     var state: String? = null
    override fun onFragmentReady() {
        initAdapters()
        onClick()
        mViewModel.apply {

            mViewModel.orderId?.let { getOrderInfo(it) }
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
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
                } else if (it.contains("aghsilini.com") == true) {
                    showToast( resources.getString(R.string.connection_error))
                }else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is OrderAction.OrderInfo -> {
                loadOrderData(action.data)
            }

            is OrderAction.AcceptOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.RejectOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }

            is OrderAction.ReciveOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
            }
            is OrderAction.DeliverOrder -> {
                showToast(action.msg)
                mViewModel.orderId?.let { mViewModel.getOrderInfo(it) }
                }

            else -> {

            }
        }
    }

    private fun loadOrderData(data: OrderInfoResponse) {
        data?.let {
            adapter.ordersList = it.order?.orderitems!!
            adapter.notifyDataSetChanged()
            binding.tvDeliveryFeesValue.setText(it.order?.delivery + " " + resources.getString(R.string.sr))
            binding.tvStatus.setText(it.order?.progress)
       //     binding.tvAddressUser.setText(it.order?.address)
      //      binding.tvClientName.setText(it.order?.customerName)
        binding.tvSubTotalValue.setText(it?.totalItemsPrice + " " + resources.getString(R.string.sr))
            binding.tvTotalValue.setText(it.order?.total + " " + resources.getString(R.string.sr))
            binding.tvAddValue.setText(it.order?.additional_cost + " " + resources.getString(R.string.sr))
            binding.tvTaxValue.setText(it.order?.tax + " " + resources.getString(R.string.sr))
            binding.tvUrgent.isVisible = (it.order?.argent == 1)
            handleStatus(it.order?.progress)

            state = it.order?.progress
            handleDeliveyData( it, it?.recive_from.toString(),it?.deliver_to.toString()  )
            binding.lytData.isVisible = true
            binding.tv1.isVisible = true
        }
    }

    private fun handleDeliveyData(
        data: OrderInfoResponse,
        recive_from: String,
        deliver_to: String
    ) {
if (recive_from == "customer"){
    binding.cardPickupClient.isVisible= true
    binding.cardPickupLaundry.isVisible= false

    binding.cardDropoffClient.isVisible= false
    binding.cardDropoffLaundry.isVisible= true
    binding.tvClientName.setText(data.order?.customerName)
    binding.tvLaundryNameDropof.setText(data.order?.laundry?.name)
    binding.tvRate.setText(data.order?.laundry?.rate)
    binding.tvLaundryLocDropof.setText(data.order?.laundry?.address)
    binding.ivLogoDropof.loadImage(data.order?.laundry?.logo, isCircular = true)




    if(state == Constants.NEW){
        binding. cardCallPickup.isVisible= false
        binding. cardLocClientPickup.isVisible= false
        binding. cardCallLaundryDropoff.isVisible= false
        binding. cardLocationDropoffLaundry.isVisible= false
    }else{
        binding. cardCallPickup.isVisible= true
        binding. cardLocClientPickup.isVisible= true
        binding. cardCallLaundryDropoff.isVisible= true
        binding. cardLocationDropoffLaundry.isVisible= true
        binding.cardLocClientPickup.setOnClickListener {
            data.order?.lat?.let { it1 -> openMap(requireContext(), it1, data.order?.lon!!) }
        }
     binding.cardCallPickup.setOnClickListener {
         data.order?.phone?.let { it1 -> call(it1) }
     }
    binding.cardLocationDropoffLaundry.setOnClickListener {
            data.order?.laundry?.lat?.let { it1 -> openMap(requireContext(), it1, data.order?.laundry?.lon!!) }
        }
     binding.cardCallLaundryDropoff.setOnClickListener {
         data.order?.laundry?.phone?.let { it1 -> call(it1) }
     }
    }

} else{

    binding.cardPickupClient.isVisible=  false
    binding.cardPickupLaundry.isVisible= true

    binding.cardDropoffClient.isVisible= true
    binding.cardDropoffLaundry.isVisible= false
    binding.tvClientNamedropoff.setText(data.order?.customerName)
    binding.tvLaundryNamePickup.setText(data.order?.laundry?.name)
    binding.tvRatepickup.setText(data.order?.laundry?.rate)
    binding.tvLaundryLocPickup.setText(data.order?.laundry?.address)
    binding.ivLogoPickup.loadImage(data.order?.laundry?.logo, isCircular = true)


    if(state == Constants.NEW){
        binding. cardCallLaundryPickup.isVisible= false
        binding. cardLocationPickupLaundry.isVisible= false
        binding. cardCallClientDropoff.isVisible= false
        binding. cardLocationClientDropoff.isVisible= false
    }else{

        binding. cardCallLaundryPickup.isVisible= true
        binding. cardLocationPickupLaundry.isVisible= true
        binding. cardCallClientDropoff.isVisible= true
        binding. cardLocationClientDropoff.isVisible= true


    binding.cardLocationClientDropoff.setOnClickListener {
            data.order?.lat?.let { it1 -> openMap(requireContext(), it1, data.order?.lon!!) }
        }
        binding.cardCallClientDropoff.setOnClickListener {
            data.order?.phone?.let { it1 -> call(it1) }
        }
        binding.cardLocationPickupLaundry.setOnClickListener {
            data.order?.laundry?.lat?.let { it1 -> openMap(requireContext(), it1, data.order?.laundry?.lon!!) }
        }
        binding.cardCallLaundryPickup.setOnClickListener {
            data.order?.laundry?.phone?.let { it1 -> call(it1) }
        }
    }

}
    }


    fun call(tel: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + tel)
        startActivity(dialIntent)
    }

    private fun handleStatus(progress: String?) {
        when (progress) {
            Constants.NEW -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.order_status_new))
                binding.tvStatus.setText(resources.getString(R.string.new_order))
                binding.lytNewOrder.isVisible = true
                binding.btnStatus.isVisible = false          }

            Constants.WAITING_DRIVER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state2))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                binding.lytNewOrder.isVisible = false
                binding.btnStatus.isVisible = true
                binding.btnStatus.setText(resources.getString(R.string.dropoff))
            }

            Constants.DRIVER_IN_WAY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state3))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                binding.lytNewOrder.isVisible = false
                binding.btnStatus.isVisible = true
                binding.btnStatus.setText(resources.getString(R.string.dropoff))

            }
            Constants.DRIVER_IN_WAY_TO_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state3))
                binding.tvStatus.setText(resources.getString(R.string.driver_on_way))
                binding.lytNewOrder.isVisible = false
                binding.btnStatus.isVisible = false

            }

            Constants.DRIVER_RECIVE_FROM_CUSTOMER -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.lytNewOrder.isVisible = false
                binding.btnStatus.isVisible = false
binding.btnStatus.setText(resources.getString(R.string.compeleted))
                binding.tvStatus.setText(resources.getString(R.string.dropoff))

            }







            Constants.LAUNDRY_RECIVE -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))
                binding.tvStatus.setText(resources.getString(R.string.compeleted))
                binding.lytNewOrder.isVisible = false
                 binding.btnStatus.isVisible = false

            }


            Constants.DRIVER_RECIVE_FROM_LAUNDRY -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state5))
                binding.lytNewOrder.isVisible = false

                binding.btnStatus.isVisible = true

                binding.btnStatus.setText(resources.getString(R.string.deliver))
                binding.tvStatus.setText(resources.getString(R.string.dropoff))


            }

            Constants.COMPLETED -> {
                binding.lytNewOrder.isVisible = false
                 binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))
 binding.btnStatus.isVisible= false
                binding.tvStatus.setText(resources.getString(R.string.compeleted))
            }

            else -> {
                binding.ivStatus.loadImage(resources.getDrawable(R.drawable.state6))
                binding.lytNewOrder.isVisible = false

                binding.tvStatus.setText(resources.getString(R.string.compeleted))
            }
        }
    }

    lateinit var parent: MainActivity
    private fun onClick() {
         parent = requireActivity() as MainActivity
        parent.showBottomNav(false)

binding.btnAgreeNewOrder.setOnClickListener {
    mViewModel.orderId?.let { it1 -> mViewModel.acceptOrder(it1) }
}
  binding.btnDeclineNewOrder.setOnClickListener {
    mViewModel.orderId?.let { it1 -> mViewModel.acceptOrder(it1) }
}
        binding.cardBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAgreeNewOrder.setOnClickListener {}
        binding.btnAgreeNewOrder.setOnClickListener {
            mViewModel.orderId?.let { it1 -> mViewModel.acceptOrder(it1) }
        }
        binding.btnDeclineNewOrder.setOnClickListener {
            mViewModel.orderId?.let { it1 -> mViewModel.rejectOrder(it1) }
        }

        binding.btnStatus.setOnClickListener {
            when (state) {
                Constants.DRIVER_RECIVE_FROM_CUSTOMER -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.deliverOrder(it1) }

                }

                Constants.DRIVER_RECIVE_FROM_LAUNDRY -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.deliverOrder(it1) }

                }

           Constants.NEW -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.reciveOrder(it1) }

                }
                Constants.DRIVER_IN_WAY -> {
                    mViewModel.orderId?.let { it1 -> mViewModel.reciveOrder(it1) }

                }
            }
        }
    }

    private fun initAdapters() {
        adapter = OrderInfoItemsAdapter()
        binding.rvOrders.init(requireContext(), adapter, 2)
        binding.rvOrders.addItemDecoration(SimpleDividerItemDecoration(requireContext()))


    }


}