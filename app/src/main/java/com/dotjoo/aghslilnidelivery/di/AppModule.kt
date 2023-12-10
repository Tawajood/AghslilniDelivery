 package com.dotjoo.aghslilnidelivery.di


import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModul {


    @Singleton
    @Provides
    fun provideFusedLocationClient(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

/*

    @Singleton
    @Provides
    fun provideUseCase(  homeRepository:  Repository): FcmUseCase =
        FcmUseCase(
            homeRepository
        )
*/


}