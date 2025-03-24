package com.example.androidtravelapp.di

import android.content.Context
import com.example.androidtravelapp.data.api.ApiModule
import com.example.androidtravelapp.data.api.AuthService
import com.example.androidtravelapp.data.api.DestinationService
import com.example.androidtravelapp.data.api.ReviewService
import com.example.androidtravelapp.data.auth.TokenManager
import com.example.androidtravelapp.data.repository.AuthRepository
import com.example.androidtravelapp.data.repository.DestinationRepository
import com.example.androidtravelapp.data.repository.ReviewRepository
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
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }
    
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return ApiModule.createAuthService()
    }
    
    @Provides
    @Singleton
    fun provideDestinationService(tokenManager: TokenManager): DestinationService {
        return ApiModule.createDestinationService { tokenManager.getAccessToken() }
    }
    
    @Provides
    @Singleton
    fun provideReviewService(tokenManager: TokenManager): ReviewService {
        return ApiModule.createReviewService { tokenManager.getAccessToken() }
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(tokenManager: TokenManager, authService: AuthService): AuthRepository {
        return AuthRepository(tokenManager, authService)
    }
    
    @Provides
    @Singleton
    fun provideDestinationRepository(destinationService: DestinationService): DestinationRepository {
        return DestinationRepository(destinationService)
    }
    
    @Provides
    @Singleton
    fun provideReviewRepository(reviewService: ReviewService): ReviewRepository {
        return ReviewRepository(reviewService)
    }
} 