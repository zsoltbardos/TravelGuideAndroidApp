package com.example.androidtravelapp.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiModule @Inject constructor() {
    companion object {
        // Updated to use the Azure API endpoint
        private const val BASE_URL = "https://travelguidesimply-ewbvb6h4cyb8emhg.westeurope-01.azurewebsites.net/"
        
        // Create an Auth Interceptor to add the Authorization header to requests
        private fun createAuthInterceptor(tokenProvider: () -> String?): Interceptor {
            return Interceptor { chain ->
                val token = tokenProvider()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
        }
        
        // Create logging interceptor
        private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Create Gson instance with custom type adapters if needed
        private val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
            .create()
        
        // Create OkHttpClient with interceptors
        private fun createOkHttpClient(tokenProvider: () -> String? = { null }): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(createAuthInterceptor(tokenProvider))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
        
        // Create Retrofit instance
        private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        
        // Create API services
        fun createDestinationService(tokenProvider: () -> String? = { null }): DestinationService {
            val okHttpClient = createOkHttpClient(tokenProvider)
            val retrofit = createRetrofit(okHttpClient)
            return retrofit.create(DestinationService::class.java)
        }
        
        fun createReviewService(tokenProvider: () -> String? = { null }): ReviewService {
            val okHttpClient = createOkHttpClient(tokenProvider)
            val retrofit = createRetrofit(okHttpClient)
            return retrofit.create(ReviewService::class.java)
        }
        
        fun createAuthService(): AuthService {
            val okHttpClient = createOkHttpClient()
            val retrofit = createRetrofit(okHttpClient)
            return retrofit.create(AuthService::class.java)
        }
    }
} 