package com.example.cameragallarystorageaccesspermission.data.request


import com.google.gson.annotations.SerializedName

data class ImageRequest(
    @SerializedName("device_model")
    val deviceModel: String,
    @SerializedName("device_no")
    val deviceNo: String,
    @SerializedName("device_platform")
    val devicePlatform: String,
    @SerializedName("device_type")
    val deviceType: Int,
    @SerializedName("device_version")
    val deviceVersion: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("lang_code")
    val langCode: String,
    @SerializedName("member_id")
    val memberId: Int
)