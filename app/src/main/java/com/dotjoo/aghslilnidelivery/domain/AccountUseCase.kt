package com.dotjoo.aghslilnidelivery.domain

 import com.dotjoo.aghslilnidelivery.base.BaseUseCase
import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
  import com.dotjoo.aghslilnidelivery.data.Repository
 import com.dotjoo.aghslilnidelivery.data.param.ChangPasswordParam
 import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@ViewModelScoped
class AccountUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
             return if (params is ChangPasswordParam) {
                     flow {
                        emit(repository.changePassword(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else if (params == 1){
                 flow {
                     emit(repository.getProfile())
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             }else if (params is UpdateProfileParam){
                 flow {
                     emit(repository.updateProfile(params))
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else {
                flow {
                    emit(repository.deleteAccount())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

         }
}





