package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_image_first.*

class imageFirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_first)

        imageViewImageFirstActivity.setOnClickListener{
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }
}