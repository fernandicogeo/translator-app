package com.example.imagepro.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

    interface ApiService {
        @GET("api/sibi-video/{videoNames}")
        suspend fun getSibiVideo(
            @Path("videoNames") videoNames: String
        ): Response<ResponseBody>

        @GET("api/bisindo-video/{videoNames}")
        suspend fun getBisindoVideo(
            @Path("videoNames") videoNames: String
        ): Response<ResponseBody>
    }