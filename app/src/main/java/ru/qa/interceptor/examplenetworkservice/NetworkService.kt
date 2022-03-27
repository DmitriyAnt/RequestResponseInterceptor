package ru.qa.interceptor.examplenetworkservice

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.qa.interceptor.interceptor.RequestResponseInterceptor

object NetworkService{
    fun getInstance(interceptor: RequestResponseInterceptor, baseUrl: String): PlaceHolderApi {

        val client: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(interceptor)

        val mRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        return mRetrofit.create(PlaceHolderApi::class.java)
    }
}