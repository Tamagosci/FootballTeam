package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
    }

    fun onClickRegister(v: View){
        val intentRegister = Intent(this, RegisterActivity::class.java)
        startActivity(intentRegister)
    }

    fun onClickLogin(v: View){
        val intentRegister = Intent(this, LoginActivity::class.java)
        startActivity(intentRegister)
    }
}