package com.example.cameragallarystorageaccesspermission.data.network

import com.example.cameragallarystorageaccesspermission.data.response.ImageResponse

interface ImageInterFace {

    fun onStarted()
    fun onSuccess(imageResponse: ImageResponse)
    fun onFailure(message: String)
}
