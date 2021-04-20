package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        buttonLoginSelectionActivity.setOnClickListener{
            val intent = Intent(this@SelectionActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonRegisterSelectionActivity.setOnClickListener{
            val intent = Intent(this@SelectionActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}