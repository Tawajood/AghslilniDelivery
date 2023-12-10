package com.dotjoo.aghslilnidelivery.ui.fragment.main.home

 import com.dotjoo.aghslilnidelivery.base.Action
 import com.dotjoo.aghslilnidelivery.data.response.AlOrdersResponse
 import com.dotjoo.aghslilnidelivery.data.response.OrderInfoResponse

sealed class OrderAction : Action {

    data class ShowLoading(val show: Boolean) : OrderAction()
    data class ShowFailureMsg(val message: String?) : OrderAction()


 data class  NewOrders(val data : AlOrdersResponse): OrderAction ()
 data class  PrevOrders(val data : AlOrdersResponse): OrderAction ()
 data class  CurrentOrders(val data : AlOrdersResponse): OrderAction ()
 data class  OrderInfo(val data : OrderInfoResponse): OrderAction ()

    data class AcceptOrder(val msg: String) : OrderAction()
    data class RejectOrder(val msg: String) : OrderAction()
    data class ReciveOrder(val msg: String) : OrderAction()
      data class DeliverOrder(val msg: String) : OrderAction()
}
