package com.example.moontrade.di

import android.content.Context
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.auth.AuthRepository
import com.example.moontrade.data.api.AuthApi
import com.example.moontrade.data.api.FetchOrdersApi
import com.example.moontrade.data.api.LeaderboardApi
import com.example.moontrade.data.api.MarketApi
import com.example.moontrade.data.api.PlaceOrdersApi
import com.example.moontrade.data.api.TournamentApi
import com.example.moontrade.data.repository.AuthRepositoryImpl
import com.example.moontrade.data.repository.LeaderboardRepository
import com.example.moontrade.data.repository.OrdersRepository
import com.example.moontrade.data.repository.TournamentRepository
import com.example.moontrade.data.repository.TradeRepository
import com.example.moontrade.session.SessionManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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


    @Provides @Singleton
    fun provideOrdersApi(retrofit: Retrofit): PlaceOrdersApi =
        retrofit.create(PlaceOrdersApi::class.java)

    @Provides @Singleton
    fun provideTradeRepository(
        api: PlaceOrdersApi
    ): TradeRepository = TradeRepository(api)


    @Provides @Singleton
    fun provideLeaderboardApi(retrofit: Retrofit): LeaderboardApi =
        retrofit.create(LeaderboardApi::class.java)

    @Provides @Singleton
    fun provideLeaderboardRepository(
        api: LeaderboardApi,
        session: SessionManager
    ): LeaderboardRepository = LeaderboardRepository(api, session)


    @Provides
    @Singleton
    fun provideUserOrdersApi(retrofit: Retrofit): FetchOrdersApi =
        retrofit.create(FetchOrdersApi::class.java)

    @Provides
    @Singleton
    fun provideOrdersRepository(
        api: FetchOrdersApi,
        session: SessionManager
    ): OrdersRepository = OrdersRepository(api, session)

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