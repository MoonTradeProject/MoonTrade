package com.example.moontrade.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.auth.AuthRepository
import com.example.moontrade.data.api.AuthApi
import com.example.moontrade.data.api.MarketApi
import com.example.moontrade.data.api.TournamentApi
import com.example.moontrade.data.repository.AuthRepositoryImpl
import com.example.moontrade.data.repository.TournamentRepository
import com.example.moontrade.session.SessionManager
import com.example.moontrade.utils.LocalDateTimeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTournamentRepository(
        api: TournamentApi,
        session: SessionManager
    ): TournamentRepository = TournamentRepository(api, session)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMarketApi(retrofit: Retrofit): MarketApi =
        retrofit.create(MarketApi::class.java)

    @Provides
    @Singleton
    fun provideTournamentApi(retrofit: Retrofit): TournamentApi =
        retrofit.create(TournamentApi::class.java)

    @Provides
    @Singleton
    fun provideAuthPreferences(
        @ApplicationContext context: Context
    ): AuthPreferences {
        return AuthPreferences(context)
    }


    @Provides
    @Singleton
    fun provideProfileStorage(
        @ApplicationContext context: Context
    ): com.example.moontrade.data.storage.ProfileStorage {
        return com.example.moontrade.data.storage.ProfileStorage(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}