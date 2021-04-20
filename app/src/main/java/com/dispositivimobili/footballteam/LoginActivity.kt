package com.dispositivimobili.footballteam

import android.content.Intent
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

    var start_email : Boolean = false
    var start_password : Boolean = false

    fun checkLogin(v: View?) {
        val email: String = editTextUserNameLoginActivity.getText().toString()
        if(!isValidEmail(email)){
            editTextUserNameLoginActivity.setError(getString(R.string.invalid_email))
        } else{
            start_email = true
        }
        val password: String = editTextPasswordLoginActivity.getText().toString()
        if(!isValidPassword(password)){
            editTextPasswordLoginActivity.setError(getString(R.string.invalid_password))
        } else{
            start_password = true
        }

        if(start_email == true && start_password == true){
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
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
