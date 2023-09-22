package com.example.cameragallarystorageaccesspermission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cameragallarystorageaccesspermission.data.network.ApiInterface
import com.example.cameragallarystorageaccesspermission.data.network.ImageInterFace
import com.example.cameragallarystorageaccesspermission.data.network.AppRepository
import com.example.cameragallarystorageaccesspermission.data.response.ImageResponse
import com.example.cameragallarystorageaccesspermission.data.viewmodel.ImageViewModel
import com.example.cameragallarystorageaccesspermission.data.viewmodel.ImageViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream


class MainActivity : AppCompatActivity(), ImageInterFace {
    lateinit var viewModel: ImageViewModel
    lateinit var btnTakeImage: Button
    lateinit var btnChoseImage: Button
    lateinit var btnSave: Button
    lateinit var image: ImageView
    var base64String: String? = null

    private val CAMERA_PERMISSION_CODE = 101
    private val GALLERY_REQUEST_CODE = 103
    private val STORAGE_PERMISSION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        clickListener()

        val api = ApiInterface()
        val repository = AppRepository(api)
        val factory = ImageViewModelFactory(this, repository)
        viewModel = ViewModelProvider(this, factory)[ImageViewModel::class.java]
        viewModel.imageInterface = this

    }

    private fun initView() {
        btnTakeImage = findViewById(R.id.btn_take_image)
        btnChoseImage = findViewById(R.id.btn_chose_image)
        btnSave = findViewById(R.id.btn_save_image)
        image = findViewById(R.id.imgView)
    }

    private fun clickListener() {
        btnTakeImage.setOnClickListener {
            requestCameraPermission()
        }
        btnChoseImage.setOnClickListener {
            requestStoragePermission()
        }
        btnSave.setOnClickListener {

            if (base64String != null) {
                viewModel.setUserProfile(base64String!!)
            } else {
                Toast.makeText(this, "Image Not Found", Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION
            )
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PERMISSION_CODE)
        Toast.makeText(this, "Camera Opened", Toast.LENGTH_LONG).show()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()

                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PERMISSION_CODE && data != null) {
            val captured: Bitmap = data.extras?.get("data") as Bitmap
            image.setImageBitmap(captured)
            base64String = bitmapToBase64(captured)

        } else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            val selectedImageUri: Uri? = data?.data!!
            if (selectedImageUri != null) {
                image.setImageURI(selectedImageUri)
                base64String = uriToBase64(this, selectedImageUri)
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun uriToBase64(context: Context, selectedImageUri: Uri): String? {

        try {
            val inputStream: InputStream? =
                context.contentResolver.openInputStream(selectedImageUri)

            if (inputStream != null) {
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return base64String
    }


    override fun onStarted() {
        Log.d("TAG", "onStarted")
    }

    override fun onSuccess(imageResponse: ImageResponse) {
        Log.d("TAG", "onSuccess")
        val img = imageResponse.data.profileImage
        Toast.makeText(this, "Save", Toast.LENGTH_LONG).show()
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("IMG", img)
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        Log.e("TAG", "onFailure")
    }

}