package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_image_first.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewRegisteredLoginActivity.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

        fun checkLogin(v: View?) {
            var start_email: Boolean = false
            var start_password: Boolean = false
            val email: String = editTextUserNameLoginActivity.getText().toString()
            if (!isValidEmail(email)) {
                start_email = false
                editTextUserNameLoginActivity.setError(getString(R.string.invalid_email))
            } else {
                start_email = true
            }
            val password: String = editTextPasswordLoginActivity.getText().toString()
            if (!isValidPassword(password)) {
                start_email = false
                editTextPasswordLoginActivity.setError(getString(R.string.invalid_password))
            } else {
                start_password = true
            }

            //loginUser(email, password)
        }

        private fun loginUser(email: String, password: String){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail: success")
                    val intent = Intent(this, PrincipalActivity::class.java)
                    startActivity(intent)
                } else{
                    //if sign in fails, display a message to the user
                    Log.w(TAG, "signInWithEmail: failure", task.exception)
                    val builder = AlertDialog.Builder(this)
                    with(builder)
                    {
                        setTitle("Authentication failed")
                        setMessage(task.exception?.message)
                        setPositiveButton("OK", null)
                        show()
                    }
                }
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
