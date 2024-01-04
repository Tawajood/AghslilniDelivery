package com.dotjoo.aghslilnidelivery.domain

 import com.dotjoo.aghslilnidelivery.base.BaseUseCase
import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
  import com.dotjoo.aghslilnidelivery.data.Repository
 import com.dotjoo.aghslilnidelivery.data.param.ActivationParam
 import com.dotjoo.aghslilnidelivery.data.param.NewOrderParam
 import com.dotjoo.aghslilnidelivery.data.param.OrderInfoParam
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class OrderUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

companion object OrderTypes{
    val CURRENT =1
     val PREV =3

}
        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params == CURRENT) {
                flow {
                    emit(repository.getCurrentOrder( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is NewOrderParam) {
                flow {
                    emit(repository.getNewOrder(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params is OrderInfoParam) {
                flow {
                    emit(repository.getOrderInfo(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params == PREV) {
                flow {
                    emit(repository.getPrevOrder())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params is ActivationParam) {
                flow {
                    emit(repository.activeAccount(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(null)
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
}


