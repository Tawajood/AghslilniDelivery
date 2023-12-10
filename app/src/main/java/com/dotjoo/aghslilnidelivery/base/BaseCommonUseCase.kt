package com.dotjoo.aghslilnidelivery.base


  import com.dotjoo.aghslilnidelivery.util.Resource
  import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


abstract class BaseCommonUseCase<RequestType : BaseResponse, in Params> {
    //map class from response to the result needed in View
    //abstract fun mapper(req: RequestType): ResultType

    //run the remote api
    abstract fun executeRemote(
        params: Params?
    ): Flow<NetworkResponse<RequestType, ErrorResponse>>

    fun handler(onResult: (Resource<RequestType>) -> Unit) =
        CoroutineExceptionHandler { _, exception ->
            onResult.invoke(Resource.loading(false))
            showFailureMessage(onResult, exception.message ?: exception.toString() )
        }

    fun <T> runFlow(resFlow: Flow<T>, onResult: (Resource<RequestType>) -> Unit): Flow<T> {
        return resFlow.catch { e ->
            showFailureMessage(onResult, e.cause?.toString() ?: e.toString()  )
        }.flowOn(Dispatchers.IO)
    }

    fun showFailureMessage(
        onResult: (Resource<RequestType>) -> Unit,
        message: String?,
     ) {
        onResult.invoke(Resource.loading(false))
        onResult.invoke(Resource.failure(message  ))
        //  message.showLogMessage()
    }
}