package com.dotjoo.aghslilnidelivery.domain


import com.dotjoo.aghslilnidelivery.base.BaseUseCase
import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
import com.dotjoo.aghslilnidelivery.data.Repository
import com.dotjoo.aghslilnidelivery.data.param.CheckOtpWithPhoneParam
import com.dotjoo.aghslilnidelivery.data.param.CheckPhoneParam
import com.dotjoo.aghslilnidelivery.data.param.LoginParams
import com.dotjoo.aghslilnidelivery.data.param.RegisterParams
import com.dotjoo.aghslilnidelivery.data.param.ResetPasswordParams
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AuthUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is LoginParams) {
                flow {
                    emit(repository.login(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params is RegisterParams) {
                flow {
                  emit(repository.register(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }    else  if (params is ResetPasswordParams) {
                flow {
                  emit(repository.resetpassword(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is CheckPhoneParam) {

                    flow {
                        emit(repository.checkPhone(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }
            else  if (params is CheckOtpWithPhoneParam) {
                flow {
                    emit(repository.checkOTpWIthPhone(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.logout())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


