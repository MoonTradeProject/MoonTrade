package com.example.moontrade.di

import android.content.Context
import com.example.moontrade.auth.AuthPreferences
import com.example.moontrade.auth.AuthRepository
import com.example.moontrade.data.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return FirebaseAuthRepository(auth)
    }

    @Provides
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferences(context)
    }
}
