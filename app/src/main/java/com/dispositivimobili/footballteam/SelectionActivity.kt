package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        buttonLogin.setOnClickListener{
            val intent = Intent(this@SelectionActivity, LoginActivity::class.java)
            intent.putExtra("selectionActivity", 0)
            startActivityForResult(intent, 1)
        }

        buttonRegister.setOnClickListener{
            val intent = Intent(this@SelectionActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}