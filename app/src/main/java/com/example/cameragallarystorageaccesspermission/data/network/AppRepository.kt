package com.example.cameragallarystorageaccesspermission.data.network

import com.example.cameragallarystorageaccesspermission.data.SafeApiRequest
import com.example.cameragallarystorageaccesspermission.data.request.ImageRequest
import com.example.cameragallarystorageaccesspermission.data.response.ImageResponse

class AppRepository(private val apiInterface: ApiInterface) : SafeApiRequest() {

    suspend fun getImage(
        headerMap: Map<String, String>, imageRequest: ImageRequest
    ): ImageResponse? {
        return apiRequest { apiInterface.getImage(headerMap, imageRequest) }
    }

}