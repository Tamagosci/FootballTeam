package com.dispositivimobili.footballteam

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun checkLogin(v: View?) {
        val email: String = editTextUserNameLoginActivity.getText().toString()
        if(!isValidEmail(email)){
            editTextUserNameLoginActivity.setError(getString(R.string.invalid_email))
        }
        val password: String = editTextPasswordLoginActivity.getText().toString()
        if(!isValidPassword(password)){
            editTextPasswordLoginActivity.setError(getString(R.string.invalid_password))
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
