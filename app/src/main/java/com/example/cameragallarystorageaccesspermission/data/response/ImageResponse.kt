package com.example.cameragallarystorageaccesspermission.data.response


import com.example.cameragallarystorageaccesspermission.data.response.Data
import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)