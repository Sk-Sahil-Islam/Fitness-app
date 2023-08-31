package com.example.fitnessapp.di

import android.app.Application
import com.example.fitnessapp.data.remote.FitnessApi
import com.example.fitnessapp.repository.FitnessRepository
import com.example.fitnessapp.sensor.MeasurableSensor
import com.example.fitnessapp.sensor.PedometerSensor
import com.example.fitnessapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFitnessRepository(
        api: FitnessApi
    ) = FitnessRepository(api)

    @Singleton
    @Provides
    fun provideFitnessApi(): FitnessApi {
        return Retrofit.Builder()
            .client(getLoggingHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(FitnessApi::class.java)
    }
    private fun getLoggingHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        })
        return builder.build()
    }

    @Provides
    @Singleton
    fun providePedometerSensor(app: Application): MeasurableSensor {
        return PedometerSensor(app)
    }
}