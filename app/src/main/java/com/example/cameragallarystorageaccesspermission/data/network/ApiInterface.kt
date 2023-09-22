package com.example.cameragallarystorageaccesspermission.data.network

import com.example.cameragallarystorageaccesspermission.util.AppConstants
import com.example.cameragallarystorageaccesspermission.data.request.ImageRequest
import com.example.cameragallarystorageaccesspermission.data.response.ImageResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiInterface {

    @POST("member/edit-profile-image-detail")
    suspend fun getImage(
        @HeaderMap headerMap: Map<String, String>,
        @Body imageRequest: ImageRequest
    ): Response<ImageResponse>

    companion object {
        operator fun invoke(): ApiInterface {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient =
                OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
                    val newRequest: Request = chain.request().newBuilder().addHeader(
                        "Authorization", "Bearer " + AppConstants.AUTHORIZATION_BEARER_TOKEN
                    ).build()
                    chain.proceed(newRequest)
                }).build()
            return Retrofit.Builder().baseUrl(AppConstants.BASE_URL_LIVE).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiInterface::class.java)
        }
    }
}

