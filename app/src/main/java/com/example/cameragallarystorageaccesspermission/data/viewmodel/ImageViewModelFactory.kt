package com.example.cameragallarystorageaccesspermission.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cameragallarystorageaccesspermission.data.network.AppRepository

class ImageViewModelFactory(private var context: Context, private var appRepository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImageViewModel(context, appRepository) as T
    }
}