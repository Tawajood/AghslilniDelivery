package com.dotjoo.aghslilnidelivery.ui.fragment.main.home

import android.app.Application
 import androidx.lifecycle.viewModelScope
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.base.BaseViewModel
import com.dotjoo.aghslilnidelivery.data.param.NewOrderParam
import com.dotjoo.aghslilnidelivery.data.param.OrderInfoParam
import com.dotjoo.aghslilnidelivery.data.response.AlOrdersResponse
import com.dotjoo.aghslilnidelivery.data.response.Order
import com.dotjoo.aghslilnidelivery.data.response.OrderInfoResponse
import com.dotjoo.aghslilnidelivery.domain.OrderActionUseCase
import com.dotjoo.aghslilnidelivery.domain.OrderActionUseCase.OrderTypes.accept
import com.dotjoo.aghslilnidelivery.domain.OrderActionUseCase.OrderTypes.deliver_order
import com.dotjoo.aghslilnidelivery.domain.OrderActionUseCase.OrderTypes.recive
import com.dotjoo.aghslilnidelivery.domain.OrderActionUseCase.OrderTypes.reject
import com.dotjoo.aghslilnidelivery.domain.OrderUseCase
import com.dotjoo.aghslilnidelivery.domain.OrderUseCase.OrderTypes.CURRENT
import com.dotjoo.aghslilnidelivery.domain.OrderUseCase.OrderTypes.PREV
import com.dotjoo.aghslilnidelivery.util.NetworkConnectivity
import com.dotjoo.aghslilnidelivery.util.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderViewModel
@Inject constructor(
    app: Application,
    val useCase: OrderUseCase,
    val useCaseOrderActions: OrderActionUseCase,
 ) : BaseViewModel<OrderAction>(app) {

    private val _cuurentOrders =
        MutableStateFlow<Resource<ArrayList<Order>>>(Resource.Progress(true))
    val current = _cuurentOrders.asStateFlow()

    private val _newOrders = MutableStateFlow<Resource<kotlin.collections.ArrayList<Order>>>(Resource.Progress(true))
    val new = _newOrders.asStateFlow()
var orderId:String? =null
    init {
         getNewOrders("29.8494216","31.3441353"  )
        getCurrentOrder()
     }

    fun getCurrentOrder() {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(OrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, CURRENT
                ) { res ->


                    when (res) {
                        is Resource.Failure -> {
                           produce(OrderAction.ShowFailureMsg(res.message.toString()))
                            viewModelScope.launch {

                                _cuurentOrders.emit(Resource.Failure(res.message.toString()  ))
                            }
                        }
                        is Resource.Progress -> {
                            produce(OrderAction.ShowLoading(res.loading))
                            viewModelScope.launch {

                                _cuurentOrders.emit(Resource.loading(res.loading, null))
                            }
                        }
                        is Resource.Success -> {
                            produce(OrderAction.CurrentOrders(res.data?.data as AlOrdersResponse))
                            viewModelScope.launch {

                                Resource.Success((res.data?.data as AlOrdersResponse).orders)
                                    ?.let { _cuurentOrders.emit(it) }
                            }
                            //     produce(OrderAction.ShowNearestLaundries(res.data.data as AllLaundriesResponse))
                        }
                    }

                }
            }

        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getPrevOrder() {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(OrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, PREV
                ) { res ->


                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))
                            viewModelScope.launch {

                                _cuurentOrders.emit(Resource.Failure(res.message.toString()  ))
                            }
                        }
                        is Resource.Progress -> {
                            produce(OrderAction.ShowLoading(res.loading))
                            viewModelScope.launch {

                                _cuurentOrders.emit(Resource.loading(res.loading, null))
                            }
                        }
                        is Resource.Success -> {
                            produce(OrderAction.CurrentOrders(res.data?.data as AlOrdersResponse))
                            viewModelScope.launch {

                                Resource.Success((res.data?.data as AlOrdersResponse).orders)
                                    ?.let { _cuurentOrders.emit(it) }
                            }
                            //     produce(OrderAction.ShowNearestLaundries(res.data.data as AllLaundriesResponse))
                        }
                    }

                }
            }

        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getNewOrders(lat:String , lon:String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,  NewOrderParam(lat, lon)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {
                            produce(OrderAction.ShowFailureMsg(res.message.toString()))

                            viewModelScope.launch {

                                _newOrders.emit(Resource.Failure(res.message.toString()))
                            }
                        }
                        is Resource.Progress -> {

                            produce(OrderAction.ShowLoading(res.loading))

                            viewModelScope.launch {

                                _newOrders.emit(Resource.loading(res.loading, null))
                            }
                        }
                        is Resource.Success -> {
                            produce(OrderAction.NewOrders(res.data?.data as AlOrdersResponse))
                            viewModelScope.launch {

                                Resource.Success((res.data?.data as AlOrdersResponse).orders)
                                    ?.let { _newOrders.emit(it) }   }
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

   fun getOrderInfo(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, OrderInfoParam(orderID)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.OrderInfo(res.data?.data as OrderInfoResponse))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun acceptOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, accept)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.AcceptOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun rejectOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, reject)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.RejectOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun reciveOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, recive)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.ReciveOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun deliverOrder(orderID: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(OrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseOrderActions.invoke(
                    viewModelScope, OrderInfoParam(orderID, deliver_order)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(OrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(OrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(OrderAction.DeliverOrder(res.data?.message as String))
                        }
                    }
                }
            }
        } else {
            produce(OrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

}






