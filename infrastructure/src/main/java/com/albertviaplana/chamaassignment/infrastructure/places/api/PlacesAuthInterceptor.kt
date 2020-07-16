package com.albertviaplana.chamaassignment.infrastructure.places.api

import okhttp3.Interceptor
import okhttp3.Response

class PlacesAuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url.newBuilder()
            .addQueryParameter("key", apiKey)
            .build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}