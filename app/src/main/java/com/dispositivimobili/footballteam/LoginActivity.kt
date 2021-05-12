package com.dispositivimobili.footballteam

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        ButtonRegisterLoginActivity.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun resetPassword(v:View){
        val email = email_adressLoginActivity.getText().toString()
        val resetMail = EditText(v.getContext())
        val passwordResetDialog = AlertDialog.Builder(v.getContext())
        passwordResetDialog.setTitle("Reset password?")
        passwordResetDialog.setMessage("Enter your email to received reset linl")
        passwordResetDialog.setView(resetMail)

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener( {
                Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            })
            .addOnFailureListener({
                Toast.makeText(this, "No reset password", Toast.LENGTH_SHORT).show()
            })
    }

    fun checkLogin(v: View?) {
        var correct_data : Boolean = false
        val email: String = email_adressLoginActivity.getText().toString()
        val password: String = passwordLoginActivity.getText().toString()

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
                        Log.d(TAG, "signInUserWithEmail: Success")
                    })
                    .addOnFailureListener({
                        Log.w(TAG, "signInUserWithEmail: Failure")
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    })
            }
        }

    private fun validatePassword(): Boolean {
        //val pass: String = passwordLoginActivity.text.toString()
        val password: String = passwordLoginActivity.getText().toString()
        val PASSWORD_VAL = "^" +
                //"(?=.*[0-9])" +       //at least 1 digit
                //"(?=.*[a-z])" +       //at least 1 lower case letter
                //"(?=.*[A-Z])" +       //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"

        /*return if (password != null && password.length >= 4) {
            true
        } else false*/

        val pattern = Pattern.compile(PASSWORD_VAL)
        val matcher = pattern.matcher(password)

        if(password.isEmpty()){
            passwordLoginActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()){
            passwordLoginActivityEsterna.setError(getString(R.string.invalid_password))
            return false
        }else{
            passwordLoginActivityEsterna.setError(null)
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val email: String = email_adressLoginActivity.text.toString()
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        if(email.isEmpty()){
            email_adressLoginActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else {
            if (!matcher.matches()) {
                email_adressLoginActivityEsterna.setError(getString(R.string.invalid_email))
                return false
            } else {
                email_adressLoginActivityEsterna.setError(null)
                return true
            }
        }
    }
}
