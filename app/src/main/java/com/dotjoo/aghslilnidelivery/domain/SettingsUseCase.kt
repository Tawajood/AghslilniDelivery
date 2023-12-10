package com.dotjoo.aghslilnidelivery.domain

 import com.dotjoo.aghslilnidelivery.base.BaseUseCase
import com.dotjoo.aghslilnidelivery.base.DevResponse
import com.dotjoo.aghslilnidelivery.base.ErrorResponse
import com.dotjoo.aghslilnidelivery.base.NetworkResponse
  import com.dotjoo.aghslilnidelivery.data.Repository
 import com.dotjoo.aghslilnidelivery.data.param.ChangPasswordParam
 import com.dotjoo.aghslilnidelivery.data.param.UpdateProfileParam
 import com.dotjoo.aghslilnidelivery.data.param.WithdrawPrams
 import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@ViewModelScoped
class SettingsUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
             return if (params ==1) {
                     flow {
                        emit(repository.getMessages())
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else if (params == 2){
                 flow {
                     emit(repository.getNotifications())
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             }else if (params is String){
                 flow {
                     emit(repository.sendMessage(params))
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else if (params ==3){
                 flow {
                     emit(repository.aboutUs())
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else if (params ==4){
                 flow {
                     emit(repository.contactUs())
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else if (params is WithdrawPrams){
                 flow {
                     emit(repository.withdraw(params.amount,params.bank_name,params.acount_number))
                 } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
             } else {
                flow {
                    emit(repository.terms())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

         }
}





