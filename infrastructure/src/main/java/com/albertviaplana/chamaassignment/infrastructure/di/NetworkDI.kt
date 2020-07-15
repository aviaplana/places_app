package com.albertviaplana.chamaassignment.infrastructure.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
        factory { provideGsonConverter() }
    }

fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        apiUrl: HttpUrl): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

fun provideGsonConverter(): GsonConverterFactory {
    val gson = GsonBuilder().apply {
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }.create()

    return GsonConverterFactory.create(gson)
}

fun provideOkHttpClient(authInterceptor: Interceptor) =
    OkHttpClient()
        .newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(getLoggingInterceptor())
        .build()

fun getLoggingInterceptor() =
    HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
