package ru.qa.interceptor.examplenetworkservice

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PlaceHolderApi {
    @GET("/posts/{id}")
    fun getPostById(
        @Path("id") id: Int,
        @Header("testGET") header: String
    ): Call<PlaceHolderData>
}