package com.atfotiad.fakestorechallenge.di


import com.atfotiad.fakestorechallenge.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
/**
 *  [NetworkModule] is a Dagger-hilt module that provides dependencies for the network layer.
 *  @property provideMoshi is a Dagger-hilt provider function that provides an instance of [Moshi].
 *  @property provideRetrofit is a Dagger-hilt provider function that provides an instance of [Retrofit].
 *  @property provideApiService is a Dagger-hilt provider function that provides an instance of [ApiService].
 *  @property provideOkHttpClient is a Dagger-hilt provider function that provides an instance of [OkHttpClient].
 *  @property provideHttpLoggingInterceptor is a Dagger-hilt provider function that provides an instance of [HttpLoggingInterceptor].
 *  @property ApiService is an interface that defines the API endpoints.[ApiService]
 * */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

}