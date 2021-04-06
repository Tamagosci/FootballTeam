package com.dispositivimobili.footballteam

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextUserName
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun checkRegister(v: View?) {
        val email: String = editTextTextEmailAddress.getText().toString()
        if (!isValidEmail(email)) {
            editTextTextEmailAddress.setError(getString(R.string.invalid_email))
        }
        val password: String = editTextTextPassword.getText().toString()
        if (!isValidPassword(password)) {
            editTextTextPassword.setError(getString(R.string.invalid_password))
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return if (password != null && password.length >= 4) {
            true
        } else false
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }
}