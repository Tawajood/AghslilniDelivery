package com.dotjoo.aghslilnidelivery.base

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.lang.reflect.Type

/**
 * A utility object to centralize the logic of converting a whysalonbarberResponse] to a whysalonbarberNetworkResponse]
 */
internal object ResponseHandler {

    /**
     * Converts the given whysalonbarberresponse] to a subclass of whysalonbarberNetworkResponse] based on different conditions.
     *
     * If the server response is successful with:
     * => a non-empty body -> NetworkResponse.Success<S, E>
     * => an empty body (and whysalonbarbersuccessBodyType] is Unit) -> NetworkResponse.Success<Unit, E>
     * => an empty body (and whysalonbarbersuccessBodyType] is not Unit) -> NetworkResponse.ServerError<E>
     *
     * @param response Retrofit's response object supplied to the call adapter
     * @param successBodyType A whysalonbarberType] representing the success body
     * @param errorConverter A retrofit converter to convert the error body into the error response type (E)
     * @param S The success body type generic parameter
     * @param E The error body type generic parameter
     */
    fun <S : Any, E : Any> handle(
        response: Response<S>,
        successBodyType: Type,
        errorConverter: Converter<ResponseBody, E>
    ): NetworkResponse<S, E> {
        val body = response.body()
        val headers = response.headers()
        val code = response.code()
        val errorBody = response.errorBody()

        return if (response.isSuccessful) {
            if (body != null) {
                NetworkResponse.Success(body, headers, code)
            } else {
                // Special case: If the response is successful and the body is null, return a successful response
                // if the service method declares the success body type as Unit. Otherwise, return a server error
                if (successBodyType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    NetworkResponse.Success(Unit, headers, code) as NetworkResponse<S, E>
                } else {
                    NetworkResponse.ServerError(null, code, headers)
                }
            }
        } else {
            val networkResponse: NetworkResponse<S, E> = try {
                val convertedBody = if (errorBody == null) {
                    null
                } else {
                    errorConverter.convert(errorBody)
                }
                NetworkResponse.ServerError(convertedBody, code, headers)
            } catch (ex: Exception) {
                NetworkResponse.UnknownError(ex, code = code, headers = headers)
            }
            networkResponse
        }
    }

}
