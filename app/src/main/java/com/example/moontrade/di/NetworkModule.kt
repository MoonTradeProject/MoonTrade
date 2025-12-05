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
import com.example.moontrade.data.api.ProfileApi
import com.example.moontrade.data.api.UserApi
import com.example.moontrade.data.repository.AssetsRepository
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Holds the single global Gson + Retrofit instances for the whole app.
 * NO other module should provide Gson or Retrofit!
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


//    fun provideOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val request = chain.request().newBuilder()
//                    .build()
//                chain.proceed(request)
//            }
//            .addInterceptor(NetworkLoggingInterceptor())
//            .build()
//    }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NetworkLoggingInterceptor())
            .build()
    }
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi =
        retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

//    @Provides
//    @Singleton
//    fun provideRetrofit(gson: Gson): Retrofit =
//        Retrofit.Builder()
//            .baseUrl("http://insectivora.eu:1010/")          // ← change if backend host differs
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://insectivora.eu:1010/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    @Suppress("unused")
    @Provides @Singleton
    fun provideAssetsApi(retrofit: Retrofit): com.example.moontrade.data.api.AssetsApi =
        retrofit.create(com.example.moontrade.data.api.AssetsApi::class.java)

    @Suppress("unused")
    @Provides @Singleton
    fun provideAssetsRepository(
        api: com.example.moontrade.data.api.AssetsApi,
        session: com.example.moontrade.session.SessionManager
    ): AssetsRepository =
        AssetsRepository(api, session)


}
