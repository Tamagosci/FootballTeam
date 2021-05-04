package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_image_first.*

class imageFirstActivity : AppCompatActivity() {

    //Variables
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var nameApp: TextView
    lateinit var textApp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_first)

        //animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim)

        nameApp = textViewImageFirstActivity_1
        textApp = textViewImageFirstActivity_2

        nameApp.setAnimation(topAnim)
        textApp.setAnimation(bottomAnim)

        imageViewImageFirstActivity.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        textViewImageFirstActivity_1.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        textViewImageFirstActivity_2.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}