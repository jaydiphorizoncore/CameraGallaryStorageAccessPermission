package com.example.cameragallarystorageaccesspermission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class SecondActivity : AppCompatActivity() {
    lateinit var setImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        setImg = findViewById(R.id.img)

        val i = intent.getStringExtra("IMG")
        Glide.with(this).load(i).into(setImg)

    }
}