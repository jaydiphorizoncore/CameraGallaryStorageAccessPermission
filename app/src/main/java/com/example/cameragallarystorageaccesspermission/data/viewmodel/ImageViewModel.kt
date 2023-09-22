package com.example.cameragallarystorageaccesspermission.data.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cameragallarystorageaccesspermission.util.AppConstants
import com.example.cameragallarystorageaccesspermission.data.network.ImageInterFace
import com.example.cameragallarystorageaccesspermission.data.network.AppRepository
import com.example.cameragallarystorageaccesspermission.data.request.ImageRequest
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(private val context: Context, private val imageRepository: AppRepository) :
    ViewModel() {

    lateinit var imageInterface: ImageInterFace

    @SuppressLint("HardwareIds")
    fun setUserProfile(userProfile: String) {

        imageInterface.onStarted()

        CoroutineScope(Dispatchers.Main).launch {

            val userId = AppConstants.USER_ID
            val token = AppConstants.TOKEN
            /* viewModelScope.launch {
                 launch {
                     prefManager.getUserId.collect {
                         if (it != null) {
                             userId = it
                         }
                     }
                 }

                 launch {
                     prefManager.getToken.collect {
                         if (it != null) {
                             token = it
                         }
                     }
                 }
             }*/
            val headers = HashMap<String, String>()
            headers[AppConstants.KEY] = token

            val imageRequest = ImageRequest(
                memberId = userId,
                deviceType = AppConstants.DEVICE_TYPE,
                deviceVersion = Build.VERSION.RELEASE,
                deviceModel = Build.MODEL,
                deviceNo = Settings.Secure.getString(
                    context.contentResolver, Settings.Secure.ANDROID_ID
                ),
                devicePlatform = AppConstants.DEVICE_PLATFORM,
                image = "data:image/jpeg;base64,$userProfile",
                langCode = "en"
            )

            try {
                val profileResponse = imageRepository.getImage(headers, imageRequest)
                if (profileResponse != null) {
                    imageInterface.onSuccess(imageResponse = profileResponse)
                }
            } catch (e: ApiException) {
                imageInterface.onFailure(e.message!!)
            } catch (e: Exception) {
                Log.e(this::class.simpleName, e.message.toString())
            }
        }
    }
}