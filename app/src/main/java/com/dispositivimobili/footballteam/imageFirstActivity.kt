package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_image_first.*
import kotlin.concurrent.thread

class imageFirstActivity : AppCompatActivity() {

    //Variabili utilizzate nel codice
    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var nameApp: TextView
    lateinit var textApp: TextView
    private var TAG = "imageFirstActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_first)

        //animazione apertura applicazione
        thread(start=true) {
            topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim)
            bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim)

            nameApp = nameappImageFirstActivity
            textApp = textImageFirstActivity

            nameApp.setAnimation(topAnim)
            textApp.setAnimation(bottomAnim)
        }

        //listener sull'immagine di sfondo
        imageImageFirstActivity.setOnClickListener {
            thread(start=true) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "click on image background, go to LoginActivity")
            }
        }

        //listener sul nome dell'app (testo)
        nameappImageFirstActivity.setOnClickListener {
            thread(start=true) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "click on text name app, go to LoginActivity")
            }
        }

        //listener sul testo in basso
        textImageFirstActivity.setOnClickListener {
            thread(start=true) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "click on text bottom, go to LoginActivity")
            }
        }
    }
}