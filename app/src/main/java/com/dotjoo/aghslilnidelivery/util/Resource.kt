package com.dotjoo.aghslilnidelivery.util

import java.io.Serializable

//sealed class Resource<T> {
    sealed class Resource<T>(val data: T ?= null, open val message: String? = null): Serializable {
        data class Progress<T>(val loading: Boolean ) : Resource<T>()
 //   data class Success<T>(val data: T) : Resource<T>()
 //   class Success<T>(val data: T): Resource<T>( )
   // class Success<T>(val data: T): Resource<T>( ), Serializable
    class Success<T>(data: T): Resource<T>(data), Serializable

    data class Failure<T>(override val message: String? ) : Resource<T>()

    companion object {
        fun <T> loading(isLoading: Boolean = true, partialData: T? = null): Resource<T> =
            Progress(isLoading )

        fun <T> success(data: T): Resource<T> = Success(data)

        fun <T> failure(e: String? ): Resource<T> = Failure(e )
    }
}
/*
sealed class Resource<T>(val data: T ?= null, val message: String? = null): Serializable {
    class Idle<T> : Resource<T>()
    class Success<T>(data: T): Resource<T>(data), Serializable
    class Error<T>(data: T? = null, message: String): Resource<T>(data, message), Serializable
    class Loading<T>(b: Boolean) : Resource<T>(), Serializable
}*/
/*sealed class Resource<T> {
    data class Progress<T>(val loading: Boolean, val partialData: T? = null) : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure<T>(val message: String?) : Resource<T>()

    companion object {
        fun <T> loading(isLoading: Boolean = true, partialData: T? = null): Resource<T> =
            Progress(isLoading, partialData)

        fun <T> success(data: T): Resource<T> = Success(data)

        fun <T> failure(e: String?): Resource<T> = Failure(e)
    }*/

