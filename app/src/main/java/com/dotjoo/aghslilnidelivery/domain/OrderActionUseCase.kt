package com.dotjoo.aghslilnidelivery.domain

import com.dotjoo.aghslilnidelivery.base.BaseUseCase
import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
import com.dotjoo.aghslilnidelivery.data.Repository
import com.dotjoo.aghslilnidelivery.data.param.NewOrderParam
import com.dotjoo.aghslilnidelivery.data.param.OrderInfoParam
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class OrderActionUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {

    companion object OrderTypes {
        val accept = 1
        val reject = 2
        val recive = 3
        val deliver_order = 6

    }

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is OrderInfoParam) {
            when (params.type) {
                accept -> {
                    flow {
                        emit(repository.acceptOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                reject -> {
                    flow {
                        emit(repository.rejectOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                recive -> {
                    flow {
                        emit(repository.reciveOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }


                deliver_order -> {
                    flow {
                        emit(repository.deliverOrder(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

                else -> {
                    flow {
                        emit(null)
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

                }
            }
        } else {
            flow {
                emit(null)
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


