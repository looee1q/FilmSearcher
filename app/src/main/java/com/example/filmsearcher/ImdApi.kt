package com.example.filmsearcher

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ImdApi {

    @GET("/en/API/SearchMovie/{apiKey}/{expression}")
    fun search(@Path("apiKey") apiKey: String,
               @Path("expression") expression: String): Call<FilmsResponse>
}