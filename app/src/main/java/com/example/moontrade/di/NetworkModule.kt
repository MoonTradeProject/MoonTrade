package com.example.moontrade.di

import com.example.moontrade.utils.LocalDateTimeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

/**
 * Holds the single global Gson + Retrofit instances for the whole app.
 * NO other module should provide Gson or Retrofit!
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")          // ← change if backend host differs
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}
