package com.example.reddittop.networking

import com.example.reddittop.models.RedditListing
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditAPI {
    @GET("/top.json")
    suspend fun getTop(@Query("after") after: String?): RedditListing

    companion object {
        private const val BASE_URL = "https://www.reddit.com/"

        operator fun invoke(): RedditAPI = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                client.addInterceptor(logging)
            }.build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RedditAPI::class.java)
    }
}