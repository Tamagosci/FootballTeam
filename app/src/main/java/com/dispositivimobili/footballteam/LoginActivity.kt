package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_image_first.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        textViewRegisteredLoginActivity.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

        fun checkLogin(v: View?) {
            var correct_data : Boolean = false
            val email: String = editTextUserNameLoginActivity.text.toString()
            val password: String = editTextPasswordLoginActivity.text.toString()

            if(!validateEmail() || !validatePassword()) {
                correct_data = false
            } else {
                correct_data = true
            }

            if(correct_data == true){
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener( {
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                    })
                    .addOnFailureListener({
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    })
            }
        }


    private fun validatePassword(): Boolean {
        val password: String = editTextPasswordLoginActivity.text.toString()
        val PASSWORD_VAL = "^" +
                //"(?=.*[0-9])" +       //at least 1 digit
                //"(?=.*[a-z])" +       //at least 1 lower case letter
                //"(?=.*[A-Z])" +       //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 8 characters
                "$"

        /*return if (password != null && password.length >= 4) {
            true
        } else false*/

        val pattern = Pattern.compile(PASSWORD_VAL)
        val matcher = pattern.matcher(password)

        if(password.isEmpty()){
            editTextPasswordLoginActivity.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()){
            editTextPasswordLoginActivity.setError(getString(R.string.invalid_password))
            return false
        }else{
            editTextPasswordLoginActivity.setError(null)
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val email: String = editTextUserNameLoginActivity.text.toString()
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        if(email.isEmpty()){
            editTextUserNameLoginActivity.setError(getString(R.string.field_not_empty))
            return false
        } else {
            if (!matcher.matches()) {
                editTextUserNameLoginActivity.setError(getString(R.string.invalid_email))
                return false
            } else {
                editTextUserNameLoginActivity.setError(null)
                return true
            }
        }
    }
}
